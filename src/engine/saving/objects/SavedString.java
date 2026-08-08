package engine.saving.objects;

import engine.json.JsonString;
import engine.json.JsonValue;

public final class SavedString extends SavedObject {
    private final String string;
    public SavedString(String string) {
        this.string = string;
    }

    public Object get() {
        return string;
    }
    public String toString() {
        return "\""+string+"\"";
    }

    @Override
    public JsonValue toJSON() {
        return new JsonString(string);
    }
}
