package engine.json;

public final class JsonNull extends JsonValue {
    private JsonNull() {}
    public static final JsonNull INSTANCE = new JsonNull();
    public String toString() {
        return "null";
    }
}
