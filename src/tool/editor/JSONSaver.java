package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
import main.DungeonWorld;
import main.EntityProducer;
import main.LoopingAudioData;
import main.World;
import tiles.Tile;

import java.util.ArrayList;

public class JSONSaver {
    private static JsonArray saveTiles(CoordinateMap<Tile> tiles) {
        var dimensions = tiles.getBounds();
        var map = new JsonArray();
        for (int x = dimensions.x; x <= dimensions.x+dimensions.y; x++) {
            var xn = new JsonNumber(x);
            for (int y = dimensions.z; y <= dimensions.z+dimensions.w; y++) {
                var optional = tiles.get(x, y);
                if (optional.isEmpty()) continue;
                var yn = new JsonNumber(y);
                var tile = optional.get();
                var m = new JsonObject();
                m.values.put("x", xn);
                m.values.put("y", yn);
                m.values.put("tile", tile.saveWithPart());
                map.values.add(m);
            }
        }
        return map;
    }
    public static JsonArray saveOnLoadEntities(ArrayList<EntityProducer> entities) {
        JsonArray array = new JsonArray();
        for (EntityProducer producer : entities) {
            JsonObject object = new JsonObject();
            object.values.put("x", new JsonNumber(producer.x()));
            object.values.put("y", new JsonNumber(producer.y()));
            object.values.put("class", new JsonString(producer.constructor().getDeclaringClass().getName()));
            array.values.add(object);
        }
        return array;
    }
    public static JsonArray saveLoopingAudio(ArrayList<LoopingAudioData> data) {
        JsonArray array = new JsonArray();
        for (var entry : data) {
            JsonObject object = new JsonObject();
            object.values.put("file", new JsonString(entry.file()));
            object.values.put("volume", new JsonNumber(entry.volume()));
            object.values.put("pitch", new JsonNumber(entry.pitch()));
            object.values.put("x", new JsonNumber(entry.x()));
            object.values.put("y", new JsonNumber(entry.y()));
            object.values.put("relative", new engine.json.JsonBoolean(entry.relative()));
            object.values.put("rolloff", new JsonNumber(entry.rolloff()));
            object.values.put("referenceDistance", new JsonNumber(entry.referenceDistance()));
            object.values.put("maxDistance", new JsonNumber(entry.maxDistance()));
            array.values.add(object);
        }
        return array;
    }
    public static JsonFile save(World world, CoordinateMap<Tile> tiles) {
        var file = new JsonFile();
        var objects = saveTiles(tiles);
        file.values.put("tiles", objects);
        file.values.put("id", new JsonString(world.getID()));
        file.values.put("type", new JsonString(world instanceof DungeonWorld ? "dungeon" : "normal"));
        file.values.put("onLoad", saveOnLoadEntities(world.onLoadEntities));
        file.values.put("brightness", new JsonNumber(world.getBrightness()));
        var audio = saveLoopingAudio(world.loopingAudioData);
        if (!audio.values.isEmpty()) file.values.put("loopingAudio", audio);
        return file;
    }
}
