package engine.saving.objects;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public abstract non-sealed class SaveFile extends SavedObject {
    public HashMap<String, SavedObject> map;
    public SaveFile() {
        this.map = new HashMap<>();
    }
    public void put(String key, SavedObject object) {
        map.put(key, object);
    }
    public abstract void writeToFile(FileWriter file) throws IOException;
    public abstract void readAndOverride(FileReader file);
    public SavedObject get(String key) {
        return map.get(key);
    }
}
