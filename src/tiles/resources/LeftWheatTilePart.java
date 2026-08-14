package tiles.resources;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class LeftWheatTilePart extends WheatTilePart {
    public LeftWheatTilePart(Vector2f position) {
        super(position);
    }

    @Override
    public void renderReady(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(WheatTilePart.LEFT, x, y, 1, 1.5f), -y);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.image(UNREADY_LEFT, x, y, 1, 1f);
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID__LEFT_WHEAT);
    }
}
