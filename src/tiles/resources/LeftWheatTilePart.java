package tiles.resources;

import main.Camera;
import org.joml.Vector2f;

public class LeftWheatTilePart extends WheatTilePart {
    public LeftWheatTilePart(Vector2f position) {
        super(position);
    }

    @Override
    public void renderReady(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(WheatTilePart.LEFT, x, y, 1, 1.5f), -y);
    }
}
