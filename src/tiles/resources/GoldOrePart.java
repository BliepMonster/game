package tiles.resources;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.LightSource;
import main.Camera;
import main.Game;
import main.ItemConstants;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

public class GoldOrePart extends HittableResourceCreator implements LightSource {
    public static final HittableResourceDescription DEFINITION = new HittableResourceDescription(
            /*respawn*/ Game.TICKS_PER_SECOND*70,
            /*id*/ ItemConstants.ITEM_GOLD_ORE,
            /*yield*/ 1,
            /*skill*/ SkillSet.SkillType.MINING,
            /*xp*/ 80,
            /*modifier*/ StatHolder.Stat.MINING_YIELD,
            /*health*/ 750,
            /*levelRequirement*/ 10,
            /*standable*/ false,
            /*powerType*/ ToolItemDefinition.PowerType.MINING
    );
    public void renderReady(int lastHit, boolean lastHitRight, int x, int y, float health, Camera camera) {
        float size = health/2+.5f;
        camera.addRenderJob(
                () -> camera.rect(
                        Game.getTickCount()-lastHit < 5 ?
                                lastHitRight ? Color.RED : Color.orange
                                : Color.yellow,
                        x+.5f-size/2,
                        y+.5f-size/2,
                        size,
                        size
                ),
                -y
        );
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.gray, x, y, 1, 1),
                -y
        );
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_GOLD_ORE);
    }
    public GoldOrePart(Vector2f position) {
        super(DEFINITION, position);
    }

    @Override
    public Color[] getChipColors() {
        return new Color[] { Color.YELLOW, new Color(220, 180, 60), new Color(160, 120, 40) };
    }

    @Override
    public float getLightLevel() {
        return .05f;
    }

    @Override
    public float getX() {
        return position.x+.5f;
    }
    public float getY() {
        return position.y+.5f;
    }
}
