package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
import entities.Entity;
import main.EntityProducer;
import main.GameContext;
import main.World;
import org.joml.Vector2f;
import org.joml.Vector2i;
import tiles.Tile;
import tiles.TilePart;

import java.lang.reflect.Constructor;

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

            var data = (JsonObject) obj.values.get("object");

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
    public static void loadWorld(JsonObject file, CoordinateMap<Tile> map, GameContext context) {

        JsonArray tiles = (JsonArray) file.values.get("tiles");
        JsonArray objects = (JsonArray) file.values.get("objects");

        loadTiles(tiles, map);
        loadObjects(objects, map, context);
    }
    public static void loadOnSpawn(World world, JsonFile file) throws ClassNotFoundException, NoSuchMethodException {
        if (!file.values.containsKey("onLoad")) return;
        var array = (JsonArray) file.values.get("onLoad");
        for (var object : array.values) {
            JsonObject o = (JsonObject) object;
            world.onLoadEntities.add(new EntityProducer(
                    (Constructor<Entity>)
                            Class.forName(((JsonString) o.values.get("class")).value)
                                    .getConstructor(Vector2f.class),
                    ((JsonNumber) o.values.get("x")).num,
                    ((JsonNumber) o.values.get("y")).num
            ));
        }
    }
}
