package tiles.resources;

import engine.core.DrawManager;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.Game;
import org.joml.Vector2f;
import main.ItemConstants;
import stats.StatHolder;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

import static java.awt.Color.*;

public class CarrotTilePart extends CropTilePart {
    public static final Color UNREADY = new Color(150, 240, 130);
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            ItemConstants.ITEM_CARROT,
            5,
            2,
            SkillSet.SkillType.FARMING, StatHolder.Stat.FARMING_YIELD, Game.TICKS_PER_SECOND*90);
    public CarrotTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public boolean isStandable() {
        return true;
    }
    public void renderReady(Camera camera, int x, int y) {
        camera.rect(orange, x, y, 1, .8f);
        camera.rect(UNREADY, x, y+.4f, 1, .4f);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.rect(UNREADY, x, y, 1, .8f);
    }
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_CARROTS);
    }
}
