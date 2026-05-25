package tool.editor;

import engine.json.*;
import tiles.*;

public class TileLoader {

    public static Tile load(JsonObject obj) {
        String type = ((JsonString) obj.values.get("type")).value;

        return switch (type) {
            case "default" -> loadDefault(obj);
            default -> throw new RuntimeException("Unknown tile type: " + type);
        };
    }

    private static Tile loadDefault(JsonObject obj) {
        String key = ((JsonString) obj.values.get("key")).value;
        return new DefaultTile(TileType.valueOf(key)); // or however you instantiate
    }
}