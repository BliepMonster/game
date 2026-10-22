package tiles.resources.ores;

import engine.json.JsonString;
import engine.json.JsonValue;
import main.LightSource;
import main.Camera;
import main.Game;
import main.TextureIdMap;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tiles.resources.HittableResourceCreator;
import tiles.resources.HittableResourceDescription;
import tool.editor.TilePartLoader;

import java.awt.*;

public class RubyGemstoneTilePart extends HittableResourceCreator implements LightSource {
    public static final HittableResourceDescription DESC = new HittableResourceDescription(
            Game.TICKS_PER_SECOND*100,
            "Ruby",
            2,
            SkillSet.SkillType.MINING,
            150,
            StatHolder.Stat.MINING_YIELD,
            900,
            12,
            false,
            ToolItemDefinition.PowerType.MINING
    );
    public RubyGemstoneTilePart(Vector2f position) {
        super(DESC, position);
    }
    public void renderReady(int ticksSinceLastHit, boolean wasLastHitSuccessFul, int x, int y, float health, Camera camera) {
        var g = Game.getTickCount()-ticksSinceLastHit >= 5;
        camera.addRenderJob(() ->
                camera.tintedImage(
                        g ? TextureIdMap.get("base:tiles:ruby") : TextureIdMap.get("base:tiles:coal_outline"),
                        g ?Color.WHITE :
                                wasLastHitSuccessFul ? Color.RED
                                : Color.WHITE,
                        x, y, 1, 1), -y);
    }
    public void renderUnready(int x, int y, Camera camera) {
        camera.addRenderJob(() ->
                camera.image(TextureIdMap.get("base:tiles:gemstone_rock"), x, y, 1, 1), -y);
    }

    @Override
    public Color[] getChipColors() {
        return new Color[] { Color.RED, new Color(180, 0, 0), new Color(120, 0, 0) };
    }

    @Override
    public float getLightLevel() {
        return this.isAlive() ? 1.2f : 0f;
    }
    @Override
    public float getX() {
        return position.x+.5f;
    }

    @Override
    public float getY() {
        return position.y+.5f;
    }
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_RUBY);
    }
}
