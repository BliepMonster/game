package tiles;

import engine.core.DrawManager;
import main.WorldContext;

public class DefaultTile extends Tile {
    private final TileType type;
    public DefaultTile(TileType type) {
        this.type = type;
    }
    @Override
    public void renderSelf(DrawManager draw, float x, float y, float w, float h) {
        type.render(draw, x, y, w, h);
    }
    @Override
    protected boolean isSelfStandable() {
        return type.standable;
    }
    public void onStand(WorldContext context) {
        part.onStand(context);
    }
}
