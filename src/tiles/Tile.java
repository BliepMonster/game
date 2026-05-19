package tiles;

import engine.core.DrawManager;

public abstract class Tile {
    protected abstract boolean isSelfStandable();
    protected TilePart part;
    public TilePart getPart() {
        return part;
    }
    public void setPart(TilePart part) {
        this.part = part;
    }
    public abstract void onStand();
    public boolean isStandable() {
        return isSelfStandable() && (part == null || part.isStandable());
    }
    protected abstract void renderSelf(DrawManager draw, float x, float y, float w, float h);
    public void render(DrawManager draw, float x, float y, float w, float h) {
        renderSelf(draw, x, y, w, h);
        if (part != null) part.render(draw, x, y, w, h);
    }
}
