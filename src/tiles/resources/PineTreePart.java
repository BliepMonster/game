package tiles.resources;

import engine.core.Texture;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.LootPiece;
import entities.LootReward;
import entities.LootTable;
import entities.LootType;
import main.Camera;
import main.Game;
import main.TextureIdMap;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;
import tool.editor.TilePartLoader;

import java.awt.*;
import java.util.HashMap;

public class PineTreePart extends HittableResourceCreator {
    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Pine Resin", 1);
        HashMap<String, Integer> map2 = new HashMap<>();
        map2.put("Cucumber Seeds", 1);
        var table = new LootTable(new LootPiece(LootType.UNCOMMON, new LootReward(0, new ItemDefinition[0], map)), new LootPiece(LootType.RARE, new LootReward(0, new ItemDefinition[0], map2)));
        DESCRIPTION = new HittableResourceDescription(
                Game.TICKS_PER_SECOND*45,
                "Pine Wood",
                2,
                SkillSet.SkillType.FORAGING,
                150,
                StatHolder.Stat.FORAGING_YIELD,
                250,
                7,
                false,
                ToolItemDefinition.PowerType.WOODCUTTING,
                table
        );
    }
    public static final HittableResourceDescription DESCRIPTION;
    public PineTreePart(Vector2f position) {
        super(DESCRIPTION, position);
    }
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:pine_tree");
    public static final Texture OUTLINE = TextureIdMap.get("base:tiles:pine_tree_overlay");
    public void renderReady(int lastHitTick, boolean wasSuccessful, int x, int y, float remainingHealth, Camera camera) {
        if (Game.getTickCount() - lastHitTick <= 5) {
            camera.addRenderJob(() -> camera.tintedImage(OUTLINE, wasSuccessful ? Color.red : Color.orange, x, y, 1, 2), -y);
        } else {
            camera.addRenderJob(() -> camera.image(TEXTURE, x-.5f, y, 2, 4.5f), -y);
        }
    }

    @Override
    public boolean allowStandingOnEmpty() {
        return true;
    }
    public void renderUnready(int x, int y, Camera camera) {}

    @Override
    public Color[] getChipColors() {
        return new Color[] { new Color(97, 64, 39), new Color(90, 60, 30), new Color(66, 97, 77) };
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_PINE_TREE);
    }
}
