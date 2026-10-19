package tiles.resources;

import engine.core.textures.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.LightSource;
import main.Camera;
import main.TextureIdMap;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

public class GlowingMushroomTilePart extends ResourceCreator implements LightSource {
    public static final ResourceDefinition DEFINITION = new ResourceDefinition(
            "Glowing Mushroom",
            24,
            1,
            SkillSet.SkillType.FORAGING,
            StatHolder.Stat.FORAGING_YIELD,
            3200
    );
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:glowing_mushroom");
    public static final Texture SHADOW = TextureIdMap.get("base:tiles:gm_shadow");

    public GlowingMushroomTilePart(Vector2f position) {
        super(DEFINITION, position);
    }
    public void renderUnready(Camera camera, int x, int y) {}

    @Override
    public void renderReady(Camera camera, int x, int y) {
        camera.image(SHADOW, x, y, 1, 1);
        camera.addRenderJob(() -> camera.image(TEXTURE, x, y+.5f, 1, 1), -y-.5f);
    }
    public boolean isStandable() {
        return true;
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_GLOWING_MUSHROOM);
    }

    @Override
    public float getX() {
        return position.x+.5f;
    }

    @Override
    public float getY() {
        return position.y+.5f;
    }

    @Override
    public float getLightLevel() {
        return super.ready ? .9f : 0;
    }
}
