package entities.particles;

import main.Camera;
import org.joml.Vector2f;

import java.awt.*;

public class SwingConeEffect extends Particle {
    final float size;
    final float angle;
    final float widthAngle;
    @Override
    public Vector2f moveSelf(Vector2f vec, float ratio) {
        return vec;
    }
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.cone(Color.white, position.x, position.y, size, angle, widthAngle, 20), 40000);
    }

    public SwingConeEffect(Vector2f position, int ticks, float size, float angle, float widthAngle) {
        super(position, ticks);
        this.size = size;
        this.angle = angle;
        this.widthAngle = widthAngle;
    }
}
