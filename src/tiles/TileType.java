package tiles;

import engine.core.Texture;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;

public enum TileType {
    GRASS(true, new Texture("res/grass_tile.png")),
    WALL(false, new Texture("res/wall_tile.png")),
    ROAD(true, new Texture("res/road_tile.png")),
    DIRT_PATH(true, new Texture("res/path_tile.png")),
    COMPOST(true, new Texture("res/compost_tile.png")),
    GRASS_DARK(true, new Texture("res/grass_dark_tile.png")),
    ;
    public final boolean standable;
    public final Texture color;
    public void render(Camera camera, int x, int y) {
        camera.image(color, x, y, 1, 1);
    }
    TileType(boolean isStandable, Texture color) {
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