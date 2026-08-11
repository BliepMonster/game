package tiles.resources;

import engine.core.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.ItemConstants;
import main.TextureIdMap;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

public class PineTreePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESCRIPTION = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*15,
            "Pine Wood",
            2,
            SkillSet.SkillType.FORAGING,
            90,
            StatHolder.Stat.FORAGING_YIELD,
            150,
            7,
            false,
            ToolItemDefinition.PowerType.WOODCUTTING
    );
    public PineTreePart(Vector2f position) {
        super(DESCRIPTION, position);
    }
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:pine_tree");
    public static final Texture OUTLINE = new Texture("res/maple_tree_outline.png");
    public void renderReady(int lastHitTick, boolean wasSuccessful, int x, int y, float remainingHealth, Camera camera) {
        if (Game.getTickCount() - lastHitTick <= 5) {
            camera.addRenderJob(() -> camera.tintedImage(OUTLINE, wasSuccessful ? Color.red : Color.orange, x, y, 1, 2), -y);
        } else {
            camera.addRenderJob(() -> camera.image(TEXTURE, x-.5f, y, 2, 4.5f), -y);
        }
    }
    public void renderUnready(int x, int y, Camera camera) {}

    @Override
    public Color[] getChipColors() {
        return new Color[] { new Color(97, 64, 39), new Color(90, 60, 30), new Color(66, 97, 77) };
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_PINE_TREE);
    }
}
