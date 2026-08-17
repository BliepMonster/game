package tiles;

import engine.json.JsonBoolean;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import main.TextureIdMap;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class TextureAdaptTilePart extends TilePart {
    private final String texture;
    private final boolean standable;

    public TextureAdaptTilePart(Vector2f position, String texture, boolean standable) {
        super(position);
        this.standable = standable;
        this.texture = texture;
    }

    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(() -> camera.image(TextureIdMap.get(texture), x, y, 1, 1), -y);
    }

    @Override
    public boolean isStandable() {
        return true;
    }

    @Override
    public void onStand(GameContext context) {

    }

    @Override
    public void onClick(GameContext context) {

    }

    @Override
    public JsonValue save() {
        var obj = new JsonObject();
        obj.values.put("type", new JsonString(TilePartLoader.ID_TEXTURE));
        obj.values.put("tex", new JsonString(texture));
        obj.values.put("standable", new JsonBoolean(standable));
        return obj;
    }
}
