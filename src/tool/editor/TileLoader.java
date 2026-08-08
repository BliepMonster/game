package tool.editor;

import engine.json.*;
import tiles.*;

public class TileLoader {

    public static Tile load(JsonObject obj) {
        String type = ((JsonString) obj.values.get("type")).value;

        return switch (type) {
            case "default" -> loadDefault(obj);
            case "over" -> loadOver(obj);
            default -> throw new RuntimeException("Unknown tile type: " + type);
        };
    }

    private static Tile loadDefault(JsonObject obj) {
        String key = ((JsonString) obj.values.get("key")).value;
        return new DefaultTile(TileType.get(key)); // or however you instantiate
    }
    private static Tile loadOver(JsonObject obj) {
        String key = ((JsonString) obj.values.get("key")).value;
        if (obj.values.containsKey("prio"))
            return new RenderOverTile(load((JsonObject) obj.values.get("over")), TileType.get(key), ((JsonNumber) obj.values.get("prio")).num);
        return new RenderOverTile(load((JsonObject) obj.values.get("over")), TileType.get(key));
    }
}