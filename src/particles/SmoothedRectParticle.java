package particles;

import main.Game;
import org.joml.Vector2f;

import java.awt.*;

public class SmoothedRectParticle extends RectParticle {
    private final Vector2f s;
    private final float T;
    private final float g;

    public SmoothedRectParticle(Vector2f position, int ticks, float w, float h, Color color,
                                Vector2f direction, float speed, float lift, float gravity) {
        super(position, ticks, w, h, color);
        this.T = ticks / (float) Game.TICKS_PER_SECOND;
        this.g = gravity;
        this.s = new Vector2f(direction).normalize().mul(speed * T);
        this.s.y += lift * T;
    }

    @Override
    public Vector2f moveSelf(Vector2f start, float ratio) {
        float t = ratio * T;
        float x = start.x + s.x * ratio;
        float y = start.y + s.y * ratio - 0.5f * g * t * t;
        return new Vector2f(x, y);
    }
}
