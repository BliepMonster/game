package tiles.resources;

import engine.core.DrawManager;
import engine.core.Texture;
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

import java.awt.*;

import static java.awt.Color.*;

public class PotatoTilePart extends CropTilePart {
    public static final Texture UNREADY = TextureIdMap.get("base:tiles:potato_unready");
    public static final Texture TEX = TextureIdMap.get("base:tiles:potato");
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            ItemConstants.ITEM_POTATO,
            7, 2,
            SkillSet.SkillType.FARMING, StatHolder.Stat.FARMING_YIELD,
            Game.TICKS_PER_SECOND*120);
    public PotatoTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public boolean isStandable() {
        return true;
    }
    public void renderReady(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(TEX, x, y, 1, 1.5f), -y-.25f);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(UNREADY, x, y, 1, 1.5f), -y-.25f);
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_POTATOES);
    }
}
