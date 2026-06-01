package entities.projectiles;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.Entity;
import main.Camera;
import org.joml.Vector2f;

import java.awt.*;

public class BasicArrowProjectile extends ArrowProjectile {
    public BasicArrowProjectile(Entity sender, Vector2f direction, int damage) {
        super(sender, direction, damage);
    }

    public void render(Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.white, position.x, position.y, .1f, .1f),
                -position.y
        );
    }
    private final Hitbox hitbox = new RectangularHitbox(0, 0, .1f, .1f, this);
    public Hitbox getHitbox() {
        return hitbox;
    }
    public float getWidth() {
        return .1f;
    }
    public float getHeight() {
        return .1f;
    }
}
