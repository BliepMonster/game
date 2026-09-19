package tiles;

import engine.core.DrawManager;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;

public abstract class TilePart {
    protected Vector2f position;
    public TilePart(Vector2f position) {
        this.position = position;
    }
    public abstract void render(int x, int y, Camera camera);
    public abstract boolean isStandable();
    public boolean isProjectileStandable() {
        return true;
    }
    public abstract void onStand(GameContext context);
    public abstract void onClick(GameContext context);
    public abstract JsonValue save();
    public boolean canBreak() {
        return true;
    }
    public ItemDefinition getItemDefinition() {
        return null;
    }
}
