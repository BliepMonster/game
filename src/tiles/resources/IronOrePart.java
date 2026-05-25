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

public class IronOrePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*15,
            ItemConstants.ITEM_IRON_ORE,
            1,
            SkillSet.SkillType.MINING,
            25,
            StatHolder.Stat.MINING_YIELD,
            500,
            5,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    public IronOrePart() {
        super(DESC);
    }
    public void renderReady(int ticksSinceLastHit, boolean wasLastHitSuccessFul, int x, int y, float health, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(Color.WHITE, x, y, 1, 1), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(Color.GRAY, x, y, 1, 1), -y);
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_IRON_ORE);
    }
}
