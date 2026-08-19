package engine.saving.objects;

import engine.json.JsonNumber;
import engine.json.JsonValue;

public final class SavedDouble extends SavedObject {
    private final double d;
    public SavedDouble(double d) {
        this.d = d;
    }
    public Object get() {
        return d;
    }
    public double getCore() {
        return d;
    }

    @Override
    public JsonValue toJSON() {
        return new JsonNumber(d);
    }
}
