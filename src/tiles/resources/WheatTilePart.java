package tiles.resources;

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

public class WheatTilePart extends CropTilePart {
    public static final Color UNREADY = new Color(128, 222, 117);
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(ItemConstants.ITEM_WHEAT, 3, 1, SkillSet.SkillType.FARMING, StatHolder.Stat.FARMING_YIELD, Game.TICKS_PER_SECOND*10);
    public WheatTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public boolean isStandable() {
        return true;
    }
    public void renderReady(Camera camera, int x, int y) {
        camera.rect(yellow, x, y, 1, .8f);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.rect(UNREADY, x, y, 1, .8f);
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_WHEAT);
    }
}
