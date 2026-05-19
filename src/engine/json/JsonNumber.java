package engine.json;

public final class JsonNumber extends JsonValue {
    public final float num;
    public JsonNumber(float f) {
        this.num = f;
    }
    public String toString() {
        return Float.toString(num);
    }
}
