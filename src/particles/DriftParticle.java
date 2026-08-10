package particles;

import main.Game;
import org.joml.Vector2f;

import java.awt.*;

public class DriftParticle extends RectParticle {
    private final Vector2f velocity;
    private final float wobble;
    private final float wobbleSpeed;
    private final float phase;
    private final float T;

    public DriftParticle(Vector2f position, int ticks, float w, float h, Color color,
                         Vector2f velocity, float wobble, float wobbleSpeed) {
        super(position, ticks, w, h, color);
        this.T = ticks / (float) Game.TICKS_PER_SECOND;
        this.velocity = velocity;
        this.wobble = wobble;
        this.wobbleSpeed = wobbleSpeed;
        this.phase = (float) (Math.random() * 2 * Math.PI);
    }

    @Override
    public Vector2f moveSelf(Vector2f start, float ratio) {
        float t = ratio * T;
        float x = start.x + velocity.x * t + wobble * (float) Math.sin(wobbleSpeed * t + phase);
        float y = start.y + velocity.y * t + wobble * (float) Math.cos(wobbleSpeed * t * .7f + phase);
        return new Vector2f(x, y);
    }
}
