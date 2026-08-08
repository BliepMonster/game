package engine.json;

import engine.saving.objects.SavedNull;
import engine.saving.objects.SavedObject;

public final class JsonNull extends JsonValue {
    private JsonNull() {}
    public static final JsonNull INSTANCE = new JsonNull();
    public String toString() {
        return "null";
    }
    public SavedObject toSavedObject() {
        return new SavedNull();
    }
}
