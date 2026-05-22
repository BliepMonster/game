package tiles;

import engine.core.DrawManager;
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
    protected abstract void renderSelf(DrawManager draw, float x, float y, float w, float h);
    public void render(DrawManager draw, float x, float y, float w, float h) {
        renderSelf(draw, x, y, w, h);
        if (part != null) part.render(draw, x, y, w, h);
    }
    public void onClick(GameContext context) {
        if (part != null) part.onClick(context);
    }
    public void onStand(GameContext context) {
        if (part != null) part.onStand(null);
        onSelfStand(context);
    }
}
