package tiles;

import engine.core.DrawManager;
import main.Camera;
import main.GameContext;

public abstract class Tile {
    protected abstract boolean isSelfStandable();
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
    protected abstract void renderSelf(Camera camera, int x, int y);
    public void render(Camera camera, int x, int y) {
        renderSelf(camera, x, y);
        if (part != null) part.render(x, y, camera);
    }
    public void onClick(GameContext context) {
        if (part != null) part.onClick(context);
    }
    public void onStand(GameContext context) {
        if (part != null) part.onStand(null);
        onSelfStand(context);
    }
}
