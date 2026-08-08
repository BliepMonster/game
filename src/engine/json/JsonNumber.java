package engine.json;

import engine.saving.objects.SavedFloat;
import engine.saving.objects.SavedObject;

public final class JsonNumber extends JsonValue {
    public final float num;
    public JsonNumber(float f) {
        this.num = f;
    }
    public String toString() {
        return Float.toString(num);
    }
    public SavedObject toSavedObject() {
        return new SavedFloat(num);
    }
}
