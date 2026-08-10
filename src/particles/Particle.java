package particles;

import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.Entity;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

public abstract class Particle implements Entity {
    protected Vector2f position;
    protected Vector2f original;
    protected final int endTick, duration;
    public Particle(Vector2f position, int ticks) {
        this.position = this.original = position;
        this.endTick = ticks + Game.getTickCount();
        this.duration = ticks;
    }
    public void move(Hitbox world) {
        // pass
    }
    public void tick(GameContext context) {
        position = moveSelf(original, 1-(endTick - (float) Game.getTickCount())/duration);
    }
    public boolean shouldDie(GameContext context) {
        return Game.getTickCount() > endTick;
    }
    public static final Hitbox hitbox = hitbox -> false;

    @Override
    public Hitbox getHitbox() {
        return hitbox;
    }

    @Override
    public void die(GameContext context) {}
    public void damage(int t, DamageType d, GameContext g) {}

    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public void knock(Vector2f direction, float amount) {}

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }
    public abstract Vector2f moveSelf(Vector2f vec, float ratio);
}
