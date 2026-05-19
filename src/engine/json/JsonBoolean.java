package engine.json;

public final class JsonBoolean extends JsonValue {
    public final boolean bool;
    public JsonBoolean(boolean b) {
        bool = b;
    }
    public String toString() {
        return Boolean.toString(bool);
    }
}
