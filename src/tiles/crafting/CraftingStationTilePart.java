package tiles.crafting;

import crafting.*;
import crafting.ui.CraftingPanel;
import engine.FileUtils;
import engine.core.textures.Texture;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.json.JsonParser;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;
import tiles.TilePart;

import java.util.List;

import static tool.editor.TilePartLoader.ID_CRAFTING_STATION;

public class CraftingStationTilePart extends TilePart {
    public CraftingStationTilePart(Vector2f position) {
        super(position);
    }
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

    public static final Texture TEXTURE = new Texture("res/textures/tiles/crafting_table.png");

    @Override
    public JsonValue save() {
        return new JsonString(ID_CRAFTING_STATION);
    }
    @Override
    public ItemDefinition getItemDefinition() {
        return ItemDefinition.get("base:placers:crafting_table");
    }
}
