package tool.editor;

import engine.CoordinateMap;
import engine.json.*;
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
                    object.values.put("object", tile.getPart().save());
                    map2.values.add(object);
                }
                map.values.add(m);
            }
        }
        return new TwoObjects(map, map2);
    }
    public static JsonFile save(CoordinateMap<Tile> tiles) {
        var file = new JsonFile();
        var objects = saveTiles(tiles);
        file.values.put("tiles", objects.o1());
        file.values.put("objects", objects.o2());
        return file;
    }
}
