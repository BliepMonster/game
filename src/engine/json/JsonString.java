package engine.json;

import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;

public final class JsonString extends JsonValue {
    public final String value;
    public JsonString(String value) {
        this.value = value;
    }
    public String toString() {
        return "\"" + value + "\"";
    }
    public SavedObject toSavedObject() {
        return new SavedString(value);
    }
}
