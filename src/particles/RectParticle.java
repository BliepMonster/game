package particles;

import main.Camera;
import org.joml.Vector2f;

import java.awt.*;

public abstract class RectParticle extends Particle {
    protected final float w, h;
    protected final Color color;

    public RectParticle(Vector2f position, int ticks, float w, float h, Color color) {
        super(position, ticks);
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public void render(Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(color, position.x, position.y, w, h), 3000);
    }
}
