package tiles;

import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;

public class WallTile extends DefaultTile {
    public WallTile(TileType type) {
        super(type);
    }

    @Override
    public JsonValue save() {
        var v = (JsonObject) super.save();
        v.values.put("type", new JsonString("wall"));
        return v;
    }
}
