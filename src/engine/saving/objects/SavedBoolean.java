package engine.saving.objects;

import engine.json.JsonBoolean;
import engine.json.JsonValue;

public final class SavedBoolean extends SavedObject {
    private final boolean bool;
    public SavedBoolean(boolean bool) {
        this.bool = bool;
    }
    public Object get() {
        return bool;
    }
    public boolean getCore() {
        return bool;
    }
    public String toString() {
        return bool+"";
    }

    @Override
    public JsonValue toJSON() {
        return new JsonBoolean(bool);
    }
}
