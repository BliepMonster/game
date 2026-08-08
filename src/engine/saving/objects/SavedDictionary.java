package engine.saving.objects;

import engine.json.JsonObject;
import engine.json.JsonValue;

import java.util.HashMap;

public final class SavedDictionary extends SavedObject {
    private final HashMap<String, SavedObject> map;
    public SavedDictionary(HashMap<String, SavedObject> map) {
        this.map = map;
    }
    public Object get() {
        return map;
    }
    public SavedObject get(String key) {
        return map.get(key);
    }
    public void remove(String key) {
        map.remove(key);
    }
    public void put(String key, SavedObject object) {
        map.put(key, object);
    }
    public String toString() {
        return map.toString();
    }
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public JsonValue toJSON() {
        JsonObject object = new JsonObject();
        for (var entry : map.entrySet()) object.values.put(entry.getKey(), entry.getValue().toJSON());
        return object;
    }
}
