package stats;

import main.Game;
import org.joml.Vector2f;

public class AcceleratedVelocityChange implements VelocityChange {
    private final Vector2f direction;
    private final float amount;
    private final int totalTicks, startTick;
    public AcceleratedVelocityChange(Vector2f direction, float amount, int timeInTicks) {
        this.direction = direction;
        this.amount = amount;
        this.totalTicks = timeInTicks;
        this.startTick = Game.getTickCount();
    }
    public Vector2f getVelocityChange() {
        int currentTick = Game.getTickCount() - startTick;
        float t = (float) currentTick / totalTicks;

        // acceleration curve: slow start, fast end
        float progress = t * t;

        float prevProgress = (float)(currentTick - 1) / totalTicks;
        prevProgress = Math.max(0, prevProgress * prevProgress);

        float delta = (progress - prevProgress) * amount;

        return new Vector2f(direction).mul(delta);
    }
    public boolean canBeRemoved() {
        return Game.getTickCount() - startTick >= totalTicks;
    }
}
