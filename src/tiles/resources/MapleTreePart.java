package tiles.resources;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.ItemConstants;
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
    public MapleTreePart() {
        super(DESCRIPTION);
    }
    public static final Color COLOR = new Color(200, 100, 0);
    public void renderReady(int lastHitTick, boolean wasSuccessful, int x, int y, float remainingHealth, Camera camera) {
        camera.addRenderJob(() -> camera.rect(Game.getTickCount() - lastHitTick > 5 ? COLOR : wasSuccessful ? Color.RED : Color.ORANGE, x, y, 1, 2), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {}

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_MAPLE_TREE);
    }
}
