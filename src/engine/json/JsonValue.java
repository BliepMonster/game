package engine.json;

import engine.saving.objects.SavedObject;

public sealed abstract class JsonValue permits
        JsonString,
        JsonNumber,
        JsonObject,
        JsonArray,
        JsonBoolean,
        JsonNull
{
    public abstract String toString();
    public abstract SavedObject toSavedObject();
}
