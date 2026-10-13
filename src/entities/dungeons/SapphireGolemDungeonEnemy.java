package entities.dungeons;

import combat.LootTableRegistry;
import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Camera;
import org.joml.Vector2f;

import java.awt.*;

public class SapphireGolemDungeonEnemy extends MeleeDungeonEnemy {
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.rect(Color.blue, getRenderX(), getRenderY(), 1.5f, 3), -position.y);
    }
    private final Hitbox hit = new RectangularHitbox(0, 0, 1.5f, .5f, this);

    @Override
    public Hitbox getHitbox() {
        return hit;
    }

    @Override
    public float getHeight() {
        return .5f;
    }
    public float getWidth() {
        return 1.5f;
    }
    public SapphireGolemDungeonEnemy(Vector2f vec) {
        super(vec, 900, 180, LootTableRegistry.get("base:sapphire_golem"), 7, 10, 50, 25, 31, 2.8f);
    }
}
