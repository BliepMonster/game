package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
import main.World;
import tiles.Tile;

record TwoObjects(JsonValue o1, JsonValue o2) {}

public class JSONSaver {
    private static TwoObjects saveTiles(CoordinateMap<Tile> tiles) {
        var dimensions = tiles.getBounds();
        var map = new JsonArray();
        var map2 = new JsonArray();
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
                m.values.put("tile", tile.save());
                if (tile.getPart() != null) {
                    var object = new JsonObject();
                    object.values.put("x", xn);
                    object.values.put("y", yn);
                    var part = tile.getPart().save();
                    if (part instanceof JsonString) {
                        var o = new JsonObject();
                        o.values.put("type", part);
                        part = o;
                    }
                    object.values.put("object", part);
                    map2.values.add(object);
                }
                map.values.add(m);
            }
        }
        return new TwoObjects(map, map2);
    }
    public static JsonFile save(World world, CoordinateMap<Tile> tiles) {
        var file = new JsonFile();
        var objects = saveTiles(tiles);
        file.values.put("tiles", objects.o1());
        file.values.put("objects", objects.o2());
        file.values.put("id", new JsonString(world.getID()));
        return file;
    }
}
