package engine.json;

import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedObject;

public final class JsonNumber extends JsonValue {
    public final double num;
    public JsonNumber(double d) {
        this.num = d;
    }
    public String toString() {
        return Double.toString(num);
    }
    public SavedObject toSavedObject() {
        return new SavedDouble(num);
    }
}
