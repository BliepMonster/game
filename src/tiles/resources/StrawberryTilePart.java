package tiles.resources;

import engine.core.DrawManager;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.Player;
import main.Camera;
import org.joml.Vector2f;
import main.Game;
import main.ItemConstants;
import stats.StatHolder;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;

import static java.awt.Color.red;

public class StrawberryTilePart extends ResourceCreator {
    public static final Color green = new Color(0, 160, 0);
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            ItemConstants.ITEM_STRAWBERRY,
            4, 1,
            SkillSet.SkillType.FORAGING, StatHolder.Stat.FORAGING_YIELD, Game.TICKS_PER_SECOND*9);
    public StrawberryTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.rect(green, x+1f/4f, y+1f/4, 1f/2, 1f/2);
    }
    public void renderReady(Camera camera, int x, int y) {
        renderUnready(camera, x, y);
        camera.rect(red, x+1f/3, y+1f/3, 1f/3, 1f/3);
    }
    public boolean isStandable() {
        return true; // players & entities can stand on strawberry bushes
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_STRAWBERRIES);
    }
}
