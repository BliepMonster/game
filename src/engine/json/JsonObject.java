package engine.json;

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
}
