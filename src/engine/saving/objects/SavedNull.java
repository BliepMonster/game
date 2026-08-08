package engine.saving.objects;

import engine.json.JsonNull;
import engine.json.JsonValue;

public final class SavedNull extends SavedObject {
    public Object get() {
        return null;
    }

    @Override
    public JsonValue toJSON() {
        return JsonNull.INSTANCE;
    }
}
