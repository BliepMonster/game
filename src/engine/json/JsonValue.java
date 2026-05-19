package engine.json;

public sealed abstract class JsonValue permits
        JsonString,
        JsonNumber,
        JsonObject,
        JsonArray,
        JsonBoolean,
        JsonNull
{
    public abstract String toString();
}
