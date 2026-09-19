package tiles;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;

import static tool.editor.TilePartLoader.ID_UNSTANDABLE;

public class UnstandableTilePart extends TilePart {
    public UnstandableTilePart(Vector2f position) {
        super(position);
    }

    @Override
    public JsonValue save() {
        return new JsonString(ID_UNSTANDABLE);
    }

    @Override
    public void render(int x, int y, Camera camera) {}

    @Override
    public boolean isStandable() {
        return false;
    }

    @Override
    public void onStand(GameContext context) {}

    @Override
    public void onClick(GameContext context) {}

    @Override
    public boolean isProjectileStandable() {
        return false;
    }
}
