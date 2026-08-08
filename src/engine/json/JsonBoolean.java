package engine.json;

import engine.saving.objects.SavedBoolean;
import engine.saving.objects.SavedObject;

public final class JsonBoolean extends JsonValue {
    public final boolean bool;
    public JsonBoolean(boolean b) {
        bool = b;
    }
    public String toString() {
        return Boolean.toString(bool);
    }
    public SavedObject toSavedObject() {
        return new SavedBoolean(bool);
    }
}
