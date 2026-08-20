package tiles.resources.ores;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import main.LightSource;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tiles.resources.HittableResourceCreator;
import tiles.resources.HittableResourceDescription;

import java.awt.*;

import static tool.editor.TilePartLoader.ID_SAPPHIRE;

public class SapphireGemstoneTilePart extends HittableResourceCreator implements LightSource {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*150,
            "Sapphire",
            2,
            SkillSet.SkillType.MINING,
            300,
            StatHolder.Stat.MINING_YIELD,
            1500,
            14,
            false,
            ToolItemDefinition.PowerType.MINING
    );

    public SapphireGemstoneTilePart(Vector2f position) {
        super(DESC, position);
    }

    public void renderReady(int lastHit, boolean lastHitRight, int x, int y, float health, Camera camera) {
        float size = health/2+.5f;
        camera.addRenderJob(
                () -> camera.rect(
                        Game.getTickCount()-lastHit < 5 ?
                                lastHitRight ? Color.black : Color.orange
                                : Color.blue,
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
    public Color[] getChipColors() {
        return new Color[]{Color.blue, new Color(0, 0, 180), new Color(0, 0, 120)};
    }

    @Override
    public JsonValue save() {
        return new JsonString(ID_SAPPHIRE);
    }
    @Override
    public float getLightLevel() {
        return this.isAlive() ? .4f : 0f;
    }
    @Override
    public float getX() {
        return position.x+.5f;
    }

    @Override
    public float getY() {
        return position.y+.5f;
    }
}
