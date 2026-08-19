package engine.saving;

import engine.json.JsonValue;
import engine.saving.objects.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class TokenizedSaveFile extends SaveFile {
    public void readAndOverride(FileReader file) {
        StringBuilder src = new StringBuilder(1024);
        String s;
        try (BufferedReader reader = new BufferedReader(file)) {
            while ((s = reader.readLine()) != null) {
                src.append(s);
            }
            s = src.toString();
            s = StringCompressor.decompress(s);
            ArrayList<SaveFileToken> tokens = new SaveFileScanner(s).scan();
            this.map = new SaveFileParser(tokens).parse();
        } catch (IOException io) {
            throw new UncheckedIOException(io);
        }
    }
    public void writeToFile(FileWriter file) throws IOException {
        StringBuilder sb = new StringBuilder(this.map.size() * 256);
        sb.append("{");
        for (Map.Entry<String, SavedObject> entry : map.entrySet()) {
            sb.append('"').append(entry.getKey()).append('"').append(parse(entry.getValue()));
        }
        file.write(StringCompressor.compress(sb.append("}").toString()));
        file.flush();
    }
    private static final StringBuilder[] TYPES = {new StringBuilder("T"), new StringBuilder("F"), new StringBuilder("N")};
    private StringBuilder parse(SavedObject object) {
        switch (object) {
            case SavedBoolean b -> {
                return b.getCore() ? TYPES[0] : TYPES[1];
            }
            case SavedString s -> {
                return new StringBuilder("\"").append(s.get()).append("\"");
            }
            case SavedNull n -> {
                return TYPES[2];
            }
            case SavedDouble f -> {
                double fl = f.getCore();
                return new StringBuilder(Double.toString(fl));
            }
            case SavedDictionary d -> {
                StringBuilder sb = new StringBuilder(map.size() * 3);
                sb.append("{");
                for (Map.Entry<String, SavedObject> entry : ((Map<String, SavedObject>) d.get()).entrySet()) {
                    sb.append('"').append(entry.getKey()).append('"').append(parse(entry.getValue()));
                }
                return sb.append("}");
            }
            case SavedArray a -> {
                ArrayList<SavedObject> array = (ArrayList<SavedObject>) a.get();
                // assume size will be approx. 3 chars per piece of data (nested arrays, dicts, numbers, etc will go higher, but this is a conservative estimate)
                StringBuilder sb = new StringBuilder(array.size() * 3);
                sb.append("[");
                for (int i = 0; i < array.size(); i++) {
                    SavedObject o = array.get(i);
                    int repeats = 1;
                    for (int j = i+1; j < array.size() && array.get(j).equals(array.get(i)); ++j) {
                        ++repeats;
                    }
                    if (repeats > 1) sb.append(encodeRepeat(repeats-1));
                    i += repeats-1;
                    sb.append(parse(o));
                    if (o instanceof SavedDouble && i != array.size()-1 && array.get(i+1) instanceof SavedDouble && (double)array.get(i+1).get() >= 0) sb.append(";");
                }
                return sb.append("]");
            }
            case SaveFile file -> {
                return parse(new SavedDictionary(file.map));
            }
        }
    }
    private String encodeRepeat(int amount) {
        StringBuilder sb = new StringBuilder();
        while (amount >= 5) {
            sb.append("_");
            amount -= 5;
        }
        while (amount >= 3) {
            sb.append("/");
            amount -= 3;
        }
        if (amount == 2) {
            sb.append("*");
            amount -= 2;
        }
        sb.append("%".repeat(amount));
        return sb.toString();
    }

    @Override
    public Object get() {
        return new SavedDictionary(map).get();
    }

    @Override
    public JsonValue toJSON() {
        return new SavedDictionary(this.map).toJSON();
    }
}
