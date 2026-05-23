package stats;

import org.joml.Vector2f;

public interface VelocityChange {
    Vector2f getVelocityChange();
    boolean canBeRemoved();
}
