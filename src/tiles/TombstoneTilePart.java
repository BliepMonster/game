package tiles;

import engine.core.textures.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class TombstoneTilePart extends TilePart {
    public TombstoneTilePart(Vector2f position) {
        super(position);
    }
    public static final Texture TEX = new Texture("res/tombstone.png");
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(TEX, x, y, 1, 1.5f),
                -y
        );
    }
    public boolean isStandable() {
        return false;
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {}
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_TOMBSTONE);
    }
}
