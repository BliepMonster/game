package tiles.resources.ores;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.ItemConstants;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tiles.resources.HittableResourceCreator;
import tiles.resources.HittableResourceDescription;
import tool.editor.TilePartLoader;

import java.awt.*;

public class CoalOrePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*20,
            ItemConstants.ITEM_COAL,
            1,
            SkillSet.SkillType.MINING,
            10,
            StatHolder.Stat.MINING_YIELD,
            100,
            0,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    private static final Color HIT_FLASH = new Color(30, 30, 30);
    public CoalOrePart(Vector2f position) {
        super(DESC, position);
    }
    public void renderReady(int ticksSinceLastHit, boolean wasLastHitSuccessFul, int x, int y, float health, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(
                        Game.getTickCount()-ticksSinceLastHit < 5 ?
                                wasLastHitSuccessFul ? Color.GRAY : HIT_FLASH
                                : Color.BLACK,
                        x, y, 1, 1), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(Color.GRAY, x, y, 1, 1), -y);
    }
    @Override
    public Color[] getChipColors() {
        return new Color[] { new Color(70, 70, 70), new Color(40, 40, 40), new Color(25, 25, 25) };
    }
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_COAL_ORE);
    }
}
