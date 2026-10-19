package tiles.resources;

import engine.core.textures.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.TextureIdMap;
import org.joml.Vector2f;
import main.ItemConstants;
import stats.StatHolder;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

public class WheatTilePart extends CropTilePart {
    public static final Texture UNREADY = TextureIdMap.get("base:tiles:wheat_ungrown");
    public static final Texture UNREADY_LEFT = TextureIdMap.get("base:tiles:wheat_left_ungrown");
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            ItemConstants.ITEM_WHEAT,
            3, 1,
            SkillSet.SkillType.FARMING, StatHolder.Stat.FARMING_YIELD, Game.TICKS_PER_SECOND*50);
    public WheatTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public boolean isStandable() {
        return true;
    }
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:wheat");
    public static final Texture LEFT = TextureIdMap.get("base:tiles:wheat_left");
    public void renderReady(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(TEXTURE, x, y, 1, 1.5f), -y);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.image(UNREADY, x, y, 1, 1f);
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_WHEAT);
    }
}
