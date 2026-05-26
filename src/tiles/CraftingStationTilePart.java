package tiles;

import crafting.*;
import crafting.json.JsonRecipeLoader;
import crafting.ui.CraftingPanel;
import engine.FileUtils;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.json.JsonObject;
import engine.json.JsonParser;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static tool.editor.TilePartLoader.ID_CRAFTING_STATION;

public class CraftingStationTilePart extends TilePart {
    public static final Color COLOR = new Color(100, 100, 0);
    private static Font FONT;
    public static final List<CraftingRecipe> RECIPES = CraftingStationLoader.load(
            new JsonParser().parse(FileUtils.readAllOrEmpty("res/crafting/crafting_table.json"))
    );
    public static void setDefaultFont(Font font) {
        FONT = font;
    }
    public static Font getDefaultFont() {
        return FONT;
    }
    public void render(int x, int y, Camera camera) {
        camera.rect(COLOR, x, y, 1, 1);
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

    @Override
    public JsonValue save() {
        return new JsonString(ID_CRAFTING_STATION);
    }
}
