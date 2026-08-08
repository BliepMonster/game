package engine.saving.objects;

import engine.json.JsonNumber;
import engine.json.JsonValue;

public final class SavedFloat extends SavedObject {
    private final float f;
    public SavedFloat(float f) {
        this.f = f;
    }
    public Object get() {
        return f;
    }
    public float getCore() {
        return f;
    }

    @Override
    public JsonValue toJSON() {
        return new JsonNumber(f);
    }
}
