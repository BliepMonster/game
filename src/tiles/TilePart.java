package tiles;

import engine.core.DrawManager;
import main.WorldContext;

public abstract class TilePart {
    public abstract void render(DrawManager draw, float x, float y, float w, float h);
    public abstract boolean isStandable();
    public abstract void onStand(WorldContext context);
    public abstract void onClick(WorldContext context);
}
