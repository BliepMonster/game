package stats;

import main.Game;
import org.joml.Vector2f;

public class LinearVelocityChange implements VelocityChange {
    private final Vector2f change;
    private final float speed;
    private final int tick;
    private final int length;
    public LinearVelocityChange(Vector2f change, float speed, int tick, int length) {
        this.change = change;
        this.speed = speed;
        this.tick = tick;
        this.length = length;
    }
    // applied per tick
    public Vector2f getVelocityChange() {
        return change.mul(speed/length, new Vector2f());
    }
    public boolean canBeRemoved() {
        return Game.getTickCount() - tick >= length;
    }
}
