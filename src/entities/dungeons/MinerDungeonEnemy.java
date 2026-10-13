package entities.dungeons;

import combat.LootTableRegistry;
import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Camera;
import main.TextureIdMap;
import org.joml.Vector2f;

import java.awt.*;

public class MinerDungeonEnemy extends MeleeDungeonEnemy {
    @Override
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.image(TextureIdMap.get("base:enemy:miner_zombie"), getRenderX(), getRenderY(), 1, 2), -position.y);
    }
    private final Hitbox hit = new RectangularHitbox(0, 0, 1, .2f, this);

    @Override
    public Hitbox getHitbox() {
        return hit;
    }

    @Override
    public float getHeight() {
        return .2f;
    }
    public float getWidth() {
        return 1;
    }
    public MinerDungeonEnemy(Vector2f vec) {
        super(vec, 300, 60, LootTableRegistry.get("base:miner_zombie"), 7, 10, 25, 18, 30, 3);
    }
}
