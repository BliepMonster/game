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
    private TilePart part;

    public TextureAdaptTilePart(Vector2f position, String texture, boolean standable, TilePart part) {
        super(position);
        this.standable = standable;
        this.texture = texture;
        this.part = part;
    }

    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(() -> camera.image(TextureIdMap.get(texture), x, y, 1, 1), -y);
        if (part != null) part.render(x, y, camera);
    }

    @Override
    public boolean isStandable() {
        return standable && (part == null || part.isStandable());
    }

    @Override
    public void onStand(GameContext context) {
        if (part != null) part.onStand(context);
    }

    @Override
    public void onClick(GameContext context) {
        if (part != null) part.onClick(context);

    }

    @Override
    public JsonValue save() {
        var obj = new JsonObject();
        obj.values.put("type", new JsonString(TilePartLoader.ID_TEXTURE));
        obj.values.put("tex", new JsonString(texture));
        obj.values.put("standable", new JsonBoolean(standable));
        if (part != null) obj.values.put("part", part.save());
        return obj;
    }

    public void setPart(TilePart chef) {
        this.part = chef;
    }
}
