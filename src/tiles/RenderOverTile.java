package tiles;

import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;

import java.util.Optional;

import static tiles.TileType.keys;

public class RenderOverTile extends Tile {
    public Tile bottom;
    public TileType type;
    public Optional<Float> priority;

    public RenderOverTile(Tile bottom, TileType type) {
        this.bottom = bottom;
        this.type = type;
        this.priority = Optional.empty();
    }
    public RenderOverTile(Tile bottom, TileType type, float priority) {
        this.bottom = bottom;
        this.type = type;
        this.priority = Optional.of(priority);
    }
    public boolean isSelfStandable() {
        return bottom.isSelfStandable() && type.standable;
    }
    protected boolean isSelfProjectileStandable() {
        return bottom.isSelfProjectileStandable() && type.projectileStandable;
    }
    public void onSelfStand(GameContext context) {
        bottom.onSelfStand(context);
    }
    public void renderSelf(Camera camera, int x, int y) {
        bottom.renderSelf(camera, x, y);
        type.renderPriority(camera, x, y, priority.isEmpty() ? -y : priority.get());
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString("over"));
        object.values.put("key", new JsonString(keys.get(type)));
        object.values.put("over", bottom.saveWithPart());
        priority.ifPresent(aFloat -> object.values.put("prio", new JsonNumber(aFloat)));
        return object;
    }

    public void setPriority(float i) {
        this.priority = Optional.of(i);
    }
}
