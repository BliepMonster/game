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

public class IronOrePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*34,
            ItemConstants.ITEM_IRON_ORE,
            1,
            SkillSet.SkillType.MINING,
            35,
            StatHolder.Stat.MINING_YIELD,
            300,
            5,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    private static final Color HIT_FLASH = new Color(230, 230, 255);
    private static final Color BASE = new Color(150, 150, 160);
    public IronOrePart(Vector2f position) {
        super(DESC, position);
    }
    public void renderReady(int ticksSinceLastHit, boolean wasLastHitSuccessFul, int x, int y, float health, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(
                        Game.getTickCount()-ticksSinceLastHit < 5 ?
                                wasLastHitSuccessFul ? HIT_FLASH : BASE
                                : Color.WHITE,
                        x, y, 1, 1), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(Color.GRAY, x, y, 1, 1), -y);
    }

    @Override
    public Color[] getChipColors() {
        return new Color[] { Color.WHITE, new Color(160, 160, 160), new Color(120, 120, 120) };
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_IRON_ORE);
    }
}
