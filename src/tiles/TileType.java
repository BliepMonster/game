package tiles;

import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;

import java.awt.*;

public enum TileType {
    GRASS(true, Color.GREEN),
    WALL(false, Color.RED),
    ROAD(true, Color.GRAY),
    DIRT_PATH(true, new Color(160, 150, 0)),
    ;
    public final boolean standable;
    public final Color color;
    public void render(Camera camera, int x, int y) {
        camera.rect(color, x, y, 1, 1);
    }
    TileType(boolean isStandable, Color color) {
        this.standable = isStandable;
        this.color = color;
    }
    public JsonValue save() {
        var object = new JsonObject();
        object.values.put("type", new JsonString("default"));
        object.values.put("key", new JsonString(this.name()));
        return object;
    }
}