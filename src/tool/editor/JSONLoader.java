package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
import entities.Entity;
import main.EntityProducer;
import main.GameContext;
import main.LoopingAudioData;
import main.World;
import org.joml.Vector2f;
import org.joml.Vector2i;
import tiles.DualTile;
import tiles.Tile;
import tiles.TilePart;

import java.lang.reflect.Constructor;

public class JSONLoader {
    private static void loadTiles(JsonArray array, CoordinateMap<Tile> map, GameContext context) {
        for (var v : array.values) {
            JsonObject obj = (JsonObject) v;

            int x = (int) ((JsonNumber) obj.values.get("x")).num;
            int y = (int) ((JsonNumber) obj.values.get("y")).num;

            JsonObject tileData = (JsonObject) obj.values.get("tile");
            Tile tile = TileLoader.load(tileData, context, x, y);

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

            if (tile instanceof DualTile) continue;

            TilePart part = TilePartLoader.load(new Vector2i(x, y), data, context);

            tile.setPart(part);
        }
    }
    public static void loadWorld(JsonFile file, CoordinateMap<Tile> map, GameContext context) {

        JsonArray tiles = (JsonArray) file.values.get("tiles");

        loadTiles(tiles, map, context);
        if (file.values.containsKey("objects"))
            loadObjects((JsonArray) file.values.get("objects"), map, context);
    }
    public static void loadWorld(JsonObject file, CoordinateMap<Tile> map, GameContext context) {

        JsonArray tiles = (JsonArray) file.values.get("tiles");

        loadTiles(tiles, map, context);
        if (file.values.containsKey("objects"))
            loadObjects((JsonArray) file.values.get("objects"), map, context);
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
                    (float) ((JsonNumber) o.values.get("x")).num,
                    (float) ((JsonNumber) o.values.get("y")).num
            ));
        }
    }
    public static void loadLoopingAudio(World world, JsonFile file) {
        if (!file.values.containsKey("loopingAudio")) return;
        var array = (JsonArray) file.values.get("loopingAudio");
        for (var value : array.values) {
            JsonObject o = (JsonObject) value;
            String file1 = ((JsonString) o.values.get("file")).value;
            float volume = o.values.containsKey("volume") ? (float) ((JsonNumber) o.values.get("volume")).num : 1f;
            float pitch = o.values.containsKey("pitch") ? (float) ((JsonNumber) o.values.get("pitch")).num : 1f;
            float x = o.values.containsKey("x") ? (float) ((JsonNumber) o.values.get("x")).num : 0f;
            float y = o.values.containsKey("y") ? (float) ((JsonNumber) o.values.get("y")).num : 0f;
            boolean relative = o.values.containsKey("relative") && ((engine.json.JsonBoolean) o.values.get("relative")).bool;
            float rolloff = o.values.containsKey("rolloff") ? (float) ((JsonNumber) o.values.get("rolloff")).num : 1f;
            float refDist = o.values.containsKey("referenceDistance") ? (float) ((JsonNumber) o.values.get("referenceDistance")).num : 1f;
            float maxDist = o.values.containsKey("maxDistance") ? (float) ((JsonNumber) o.values.get("maxDistance")).num : 100f;
            world.loopingAudioData.add(new LoopingAudioData(file1, volume, pitch, x, y, relative, rolloff, refDist, maxDist));
        }
    }
}
