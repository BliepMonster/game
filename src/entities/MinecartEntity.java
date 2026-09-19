package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.projectiles.Projectile;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public class MinecartEntity extends Projectile {
    public static final float SPEED = 10f / Game.TICKS_PER_SECOND;
    public static final int DAMAGE = 50;
    private float spawned = Game.getTickCount();
    private final Vector2f movement;

    public MinecartEntity(Vector2f position) {
        super(position);
        this.movement = new Vector2f(0, -SPEED);
    }

    @Override
    public void move(Hitbox world) {
        super.move(world);
        if (Game.getTickCount()-spawned < 5) dead = false;
    }

    @Override
    public void moveSelf(Hitbox world) {
        position.add(movement);
        if (position.y <= 0) dead = true;
    }

    @Override
    public void damage(Entity entity, GameContext context) {
        if (Game.getTickCount()-spawned < 5) return;
        if (dead) return;
        dead = true;
    }

    @Override
    public void die(GameContext context) {
        context.spawn(new ExplosionEntity(DAMAGE, 4, Game.getTickCount(), 2.5f, this.position, context));
    }

    @Override
    public void render(Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.orange, getRenderX() - 1, getRenderY() - 1, 2, 2),
                -position.y
        );
    }

    private final Hitbox hitbox = new RectangularHitbox(-1, -1, 2, 2, this);

    @Override
    public Hitbox getHitbox() {
        return hitbox;
    }

    public float getWidth() {
        return 2;
    }

    public float getHeight() {
        return 2;
    }
}