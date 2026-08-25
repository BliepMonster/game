package entities;

import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.VelocityChange;

import java.util.ArrayList;

public abstract class Enemy implements Entity {
    public final ArrayList<VelocityChange> changes = new ArrayList<>();
    public final Vector2f position;
    public Enemy(Vector2f position) {
        this.position = position;
    }
    public abstract float getKnockbackResistance();
    private GameContext context;
    private final Vector2f velocityScratch = new Vector2f();
    protected GameContext getContext() {
        return context;
    }
    public Vector2f getVelocityChange() {
        velocityScratch.set(0, 0);
        changes.removeIf(VelocityChange::canBeRemoved);
        for (var change : changes) {
            velocityScratch.add(change.getVelocityChange());
        }
        return velocityScratch;
    }
    public void tick(GameContext context) {
        this.context = context;
        tickSelf(context);
    }
    public abstract void tickSelf(GameContext context);
    public boolean isAttackable() {
        return true;
    }

    @Override
    public void knock(Vector2f direction, float amount) {
        changes.add(Entity.getVelocityChange(direction, amount/getKnockbackResistance(), (int) ((float) Game.TICKS_PER_SECOND/10*Math.max(amount/5/getKnockbackResistance(), 1))));
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
}
