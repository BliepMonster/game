package tool.editor;

import engine.json.*;
import main.GameContext;
import tiles.*;

public class TileLoader {

    public static Tile load(JsonObject obj, GameContext context) {
        String type = ((JsonString) obj.values.get("type")).value;

        return switch (type) {
            case "default" -> loadDefault(obj);
            case "over" -> loadOver(obj,
                    context);
            case "wall" -> new WallTile(loadDefault(obj).type);
            case "dual" -> new DualTile(
                    load((JsonObject) obj.values.get("a"),
                            context),
                    load((JsonObject) obj.values.get("b"),
                            context),
                    ((JsonString) obj.values.get("attrib")).value,
                    context
            );
            default -> throw new RuntimeException("Unknown tile type: " + type);
        };
    }

    private static DefaultTile loadDefault(JsonObject obj) {
        String key = ((JsonString) obj.values.get("key")).value;
        return new DefaultTile(TileType.get(key)); // or however you instantiate
    }
    private static Tile loadOver(JsonObject obj, GameContext context) {
        String key = ((JsonString) obj.values.get("key")).value;
        if (obj.values.containsKey("prio"))
            return new RenderOverTile(load((JsonObject) obj.values.get("over"),
                    context), TileType.get(key), ((JsonNumber) obj.values.get("prio")).num);
        return new RenderOverTile(load((JsonObject) obj.values.get("over"),
                context), TileType.get(key));
    }
}