package tiles;

import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import engine.saving.ObjectSaveable;
import main.Camera;
import main.GameContext;

public abstract class Tile {
    protected abstract boolean isSelfStandable();
    protected abstract boolean isSelfProjectileStandable();
    protected TilePart part;
    public TilePart getPart() {
        return part;
    }
    public void setPart(TilePart part) {
        this.part = part;
    }
    public abstract void onSelfStand(GameContext context);
    public boolean isStandable() {
        return isSelfStandable() && (part == null || part.isStandable());
    }
    public boolean isProjectileStandable() {
        return isSelfProjectileStandable() && (part == null || part.isProjectileStandable());
    }
    protected abstract void renderSelf(Camera camera, int x, int y);
    public void render(Camera camera, int x, int y) {
        renderSelf(camera, x, y);
        if (part != null) part.render(x, y, camera);
    }
    public void onClick(GameContext context) {
        if (part != null) part.onClick(context);
    }
    public void onStand(GameContext context) {
        if (part != null) part.onStand(context);
        onSelfStand(context);
    }
    public abstract JsonValue save();

    public JsonValue saveWithPart() {
        JsonValue tileData = save();
        if (part != null && tileData instanceof JsonObject obj) {
            JsonValue partData = part.save();
            if (partData instanceof JsonString s) {
                var wrapped = new JsonObject();
                wrapped.values.put("type", s);
                partData = wrapped;
            }
            obj.values.put("part", partData);
        }
        return tileData;
    }
}
