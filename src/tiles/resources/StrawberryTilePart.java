package tiles.resources;

import engine.core.DrawManager;
import engine.core.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.Player;
import main.Camera;
import main.TextureIdMap;
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
    public static final Texture READY = TextureIdMap.get("base:tiles:strawberry"), UNREADY = TextureIdMap.get("base:tiles:strawberry2");
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            ItemConstants.ITEM_STRAWBERRY,
            4, 2,
            SkillSet.SkillType.FORAGING, StatHolder.Stat.FORAGING_YIELD, Game.TICKS_PER_SECOND*9);
    public StrawberryTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public void renderUnready(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(UNREADY, x, y, 1, 1.5f), -y-.5f);
    }
    public void renderReady(Camera camera, int x, int y) {
        camera.addRenderJob(() -> camera.image(READY, x, y, 1, 1.5f), -y-.5f);
    }
    public boolean isStandable() {
        return true; // players & entities can stand on strawberry bushes
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_STRAWBERRIES);
    }
}
