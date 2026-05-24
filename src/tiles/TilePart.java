package tiles;

import engine.core.DrawManager;
import main.Camera;
import main.GameContext;

public abstract class TilePart {
    public abstract void render(int x, int y, Camera camera);
    public abstract boolean isStandable();
    public abstract void onStand(GameContext context);
    public abstract void onClick(GameContext context);
}
