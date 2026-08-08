package tiles;

import engine.json.JsonValue;
import main.Camera;
import main.GameContext;

public class DefaultTile extends Tile {
    private final TileType type;
    public DefaultTile(TileType type) {
        this.type = type;
    }
    @Override
    public void renderSelf(Camera camera, int x, int y) {
        type.render(camera, x, y);
    }
    @Override
    protected boolean isSelfStandable() {
        return type.standable;
    }
    public void onSelfStand(GameContext context) {}
    public JsonValue save() {
        return type.save();
    }
}
