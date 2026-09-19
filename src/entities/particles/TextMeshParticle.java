package entities.particles;

import engine.core.graphics.TextMesh;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public class TextMeshParticle extends Particle {
    private final TextMesh mesh;
    private final Color color;

    public TextMeshParticle(Vector2f position, int ticks, TextMesh mesh, Color color) {
        super(position, ticks);
        this.mesh = mesh;
        this.color = color;
    }

    @Override
    public Vector2f moveSelf(Vector2f vec, float ratio) {
        return vec.add(0, ratio, new Vector2f());
    }

    @Override
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.renderAsText(this.mesh, color, position.x, position.y), 90000);
    }

    @Override
    public void die(GameContext context) {
        super.die(context);
        this.mesh.destroy();
    }
}
