package engine.json;

import engine.saving.objects.SaveFile;
import engine.saving.TokenizedSaveFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JsonFile {
    public final HashMap<String, JsonValue> values = new HashMap<>();
    public void write(FileWriter writer) throws IOException {
        StringBuilder sb = new StringBuilder("{");
        for (var entry : values.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",");
        }
        if (!values.isEmpty())
            sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        writer.write(sb.toString());
        writer.flush();
    }
    public SaveFile toSaveFile() {
        SaveFile file = new TokenizedSaveFile();
        for (var entry : values.entrySet()) {
            file.put(entry.getKey(), entry.getValue().toSavedObject());
        }
        return file;
    }
}
