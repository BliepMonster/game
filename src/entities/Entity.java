package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.Positioned;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.AcceleratedVelocityChange;
import stats.LinearVelocityChange;
import stats.VelocityChange;

public interface Entity extends Positioned {
    void move(Hitbox world);
    void tick(GameContext context);
    void render(Camera camera);
    boolean shouldDie(GameContext context);
    Hitbox getHitbox();
    Hitbox getWalkHitbox();
    void die(GameContext context);
    void damage(int damage, DamageType type, GameContext context);
    boolean isAttackable();
    void knock(Vector2f direction, float amount);
    float getWidth();
    float getHeight();
    static VelocityChange getVelocityChange(Vector2f direction, float amount, int timeInTicks) {
        if (direction.x == 0 && direction.y == 0) return new LinearVelocityChange(new Vector2f(), 0, 0, 0);
        if (amount > 5) return new AcceleratedVelocityChange(direction, amount, timeInTicks);
        return new LinearVelocityChange(direction, amount, Game.getTickCount(), timeInTicks);
    }
}
