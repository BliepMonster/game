package engine.json;

public final class JsonString extends JsonValue {
    public final String value;
    public JsonString(String value) {
        this.value = value;
    }
    public String toString() {
        return "\"" + value + "\"";
    }
}
