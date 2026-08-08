package engine.json;

import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedObject;

import java.util.HashMap;

public final class JsonObject extends JsonValue {
    public final HashMap<String, JsonValue> values = new HashMap<>();
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (var entry : values.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",");
        }
        if (!values.isEmpty())
            sb.deleteCharAt(sb.length() - 1);
        return sb.append("}").toString();
    }
    public SavedObject toSavedObject() {
        HashMap<String, SavedObject> map = new HashMap<>();
        for (var entry : values.entrySet()) map.put(entry.getKey(), entry.getValue().toSavedObject());
        return new SavedDictionary(map);
    }
}
