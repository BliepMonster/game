package engine.json;

import java.util.ArrayList;

public final class JsonArray extends JsonValue {
    public final ArrayList<JsonValue> values = new ArrayList<>();
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (var value : values) {
            sb.append(value).append(",");
        }
        if (!values.isEmpty())
            sb.deleteCharAt(sb.length() - 1);
        return sb.append("]").toString();
    }
}
