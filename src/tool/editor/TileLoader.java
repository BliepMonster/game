package tool.editor;

import engine.json.*;
import main.GameContext;
import org.joml.Vector2i;
import tiles.*;

public class TileLoader {

    public static Tile load(JsonObject obj, GameContext context) {
        return load(obj, context, 0, 0);
    }

    public static Tile load(JsonObject obj, GameContext context, int x, int y) {
        String type = ((JsonString) obj.values.get("type")).value;

        Tile tile = switch (type) {
            case "default" -> loadDefault(obj);
            case "over" -> loadOver(obj,
                    context, x, y);
            case "wall" -> new WallTile(loadDefault(obj).type);
            case "dual" -> new DualTile(
                    load((JsonObject) obj.values.get("a"),
                            context, x, y),
                    load((JsonObject) obj.values.get("b"),
                            context, x, y),
                    ((JsonString) obj.values.get("attrib")).value,
                    context
            );
            default -> throw new RuntimeException("Unknown tile type: " + type);
        };

        if (obj.values.containsKey("part") && !(obj.values.get("part") instanceof JsonNull)) {
            tile.setPart(TilePartLoader.load(new Vector2i(x, y), (JsonObject) obj.values.get("part"), context));
        }

        return tile;
    }

    private static DefaultTile loadDefault(JsonObject obj) {
        String key = ((JsonString) obj.values.get("key")).value;
        return new DefaultTile(TileType.get(key));
    }
    private static Tile loadOver(JsonObject obj, GameContext context, int x, int y) {
        String key = ((JsonString) obj.values.get("key")).value;
        if (obj.values.containsKey("prio"))
            return new RenderOverTile(load((JsonObject) obj.values.get("over"),
                    context, x, y), TileType.get(key), ((JsonNumber) obj.values.get("prio")).num);
        return new RenderOverTile(load((JsonObject) obj.values.get("over"),
                context, x, y), TileType.get(key));
    }
}