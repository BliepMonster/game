package tiles.resources;

import engine.core.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.ItemConstants;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

public class MapleTreePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESCRIPTION = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*5,
            ItemConstants.ITEM_MAPLE_WOOD,
            2,
            SkillSet.SkillType.FORAGING,
            15,
            StatHolder.Stat.FORAGING_YIELD,
            50,
            2,
            false,
            ToolItemDefinition.PowerType.WOODCUTTING
    );
    public MapleTreePart(Vector2f position) {
        super(DESCRIPTION, position);
    }
    public static final Texture TEXTURE = new Texture("res/maple_tree.png");
    public static final Texture OUTLINE = new Texture("res/maple_tree_outline.png");
    public void renderReady(int lastHitTick, boolean wasSuccessful, int x, int y, float remainingHealth, Camera camera) {
        if (Game.getTickCount() - lastHitTick <= 5) {
            camera.addRenderJob(() -> camera.tintedImage(OUTLINE, wasSuccessful ? Color.red : Color.orange, x, y, 1, 2), -y);
        } else {
            camera.addRenderJob(() -> camera.image(TEXTURE, x, y, 1, 2), -y);
        }
    }
    public void renderUnready(int x, int y, Camera camera) {}

    @Override
    public Color[] getChipColors() {
        return new Color[] { new Color(140, 90, 50), new Color(90, 60, 30), new Color(90, 140, 60) };
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_MAPLE_TREE);
    }
}
