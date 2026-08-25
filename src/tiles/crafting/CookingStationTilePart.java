package tiles.crafting;

import crafting.*;
import crafting.ui.CraftingPanel;
import engine.FileUtils;
import engine.core.Texture;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.json.JsonParser;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import main.TextureIdMap;
import org.joml.Vector2f;
import tiles.TilePart;

import java.util.List;

import static tool.editor.TilePartLoader.ID_COOKING_STATION;

public class CookingStationTilePart extends TilePart {
    public CookingStationTilePart(Vector2f position) {
        super(position);
    }
    private static Font FONT;
    public static final List<CraftingRecipe> RECIPES = CraftingStationLoader.load(
            new JsonParser().parse(FileUtils.readAllOrEmpty("res/crafting/cooking_station.json"))
    );
    public static void setDefaultFont(Font font) {
        FONT = font;
    }
    public static Font getDefaultFont() {
        return FONT;
    }
    public void render(int x, int y, Camera camera) {
        camera.image(TEXTURE, x, y, 1, 1);
    }
    public boolean isStandable() {
        return false;
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {
        var player = context.getPlayer();
        var inv = new FullItemStorage(player.items, player.inventory, player.currency);
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new CraftingPanel(RECIPES, inv, context, FONT, 0), 0);
            GUI.toggle(62);
        }
    }

    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:cooking_station");

    @Override
    public JsonValue save() {
        return new JsonString(ID_COOKING_STATION);
    }
}
