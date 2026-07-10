package tiles.resources;

import engine.json.JsonString;
import engine.json.JsonValue;
import lighting.LightSource;
import main.Camera;
import main.Game;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

public class RubyGemstoneTilePart extends HittableResourceCreator implements LightSource {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*120,
            "Ruby",
            2,
            SkillSet.SkillType.MINING,
            150,
            StatHolder.Stat.MINING_YIELD,
            3000,
            12,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    public RubyGemstoneTilePart(Vector2f position) {
        super(DESC, position);
    }
    public void renderReady(int lastHit, boolean lastHitRight, int x, int y, float health, Camera camera) {
        float size = health/2+.5f;
        camera.addRenderJob(
                () -> camera.rect(
                        Game.getTickCount()-lastHit < 5 ?
                                lastHitRight ? Color.black : Color.orange
                                : Color.red,
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
    public float getLightLevel() {
        return this.isAlive() ? .8f : 0f;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_RUBY);
    }
}
