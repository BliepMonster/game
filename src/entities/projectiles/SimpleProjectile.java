package entities.projectiles;

import engine.hitboxes.Hitbox;
import main.GameContext;
import org.joml.Vector2f;

public abstract class SimpleProjectile extends Projectile {
    protected final Vector2f movement;
    public SimpleProjectile(Vector2f position, Vector2f movement) {
        super(position);
        this.movement = movement;
    }
    public void moveSelf(Hitbox world) {
        position.add(movement);
    }
    public void die(GameContext context) {}
}
