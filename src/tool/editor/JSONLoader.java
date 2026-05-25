package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
import main.GameContext;
import org.joml.Vector2i;
import tiles.Tile;
import tiles.TilePart;

public class JSONLoader {
    private static void loadTiles(JsonArray array, CoordinateMap<Tile> map) {
        for (var v : array.values) {
            JsonObject obj = (JsonObject) v;

            int x = (int) ((JsonNumber) obj.values.get("x")).num;
            int y = (int) ((JsonNumber) obj.values.get("y")).num;

            JsonObject tileData = (JsonObject) obj.values.get("tile");
            Tile tile = TileLoader.load(tileData); // IMPORTANT

            map.put(x, y, tile);
        }
    }

    private static void loadObjects(JsonArray array, CoordinateMap<Tile> map, GameContext context) {
        for (JsonValue v : array.values) {
            JsonObject obj = (JsonObject) v;

            int x = (int) ((JsonNumber) obj.values.get("x")).num;
            int y = (int) ((JsonNumber) obj.values.get("y")).num;

            var data = (JsonString) obj.values.get("object");

            Tile tile = map.get(x, y)
                    .orElseThrow(() -> new RuntimeException("No tile at " + x + "," + y));

            TilePart part = TilePartLoader.load(new Vector2i(x, y), data, context);

            tile.setPart(part);
        }
    }
    public static void loadWorld(JsonFile file, CoordinateMap<Tile> map, GameContext context) {

        JsonArray tiles = (JsonArray) file.values.get("tiles");
        JsonArray objects = (JsonArray) file.values.get("objects");

        loadTiles(tiles, map);
        loadObjects(objects, map, context);
    }

}
