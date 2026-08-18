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

public class CopperOrePart extends HittableResourceCreator {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*25,
            ItemConstants.ITEM_COPPER_ORE,
            1,
            SkillSet.SkillType.MINING,
            22  ,
            StatHolder.Stat.MINING_YIELD,
            200,
            2,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    public CopperOrePart(Vector2f position) {
        super(DESC, position);
    }
    public void renderReady(int ticksSinceLastHit, boolean wasLastHitSuccessFul, int x, int y, float health, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(
                        Game.getTickCount()-ticksSinceLastHit < 5 ?
                                wasLastHitSuccessFul ? new Color(255, 190, 130) : Color.ORANGE
                                : Color.ORANGE,
                        x, y, 1, 1), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(() ->
                camera.rect(Color.GRAY, x, y, 1, 1), -y);
    }

    @Override
    public Color[] getChipColors() {
        return new Color[] { new Color(180, 90, 40), new Color(120, 60, 30), new Color(90, 50, 25) };
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_COPPER_ORE);
    }
}
