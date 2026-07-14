package tiles.crafting;

import crafting.*;
import crafting.ui.CraftingPanel;
import engine.FileUtils;
import engine.core.Texture;
import engine.gui.GUI;
import engine.json.JsonParser;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import tiles.TilePart;

import java.awt.Color;
import java.util.List;

import static tool.editor.TilePartLoader.ID_FURNACE;

public class FurnaceTilePart extends TilePart {
    public FurnaceTilePart(Vector2f position) {
        super(position);
    }
    public static final Texture TEXTURE = new Texture("res/textures/tiles/furnace.png");
    public static final List<CraftingRecipe> RECIPES = CraftingStationLoader.load(
            new JsonParser().parse(FileUtils.readAllOrEmpty("res/crafting/furnace.json"))
    );
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
            GUI.add(new CraftingPanel(RECIPES, inv, context, CraftingStationTilePart.getDefaultFont(), 0), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        return new JsonString(ID_FURNACE);
    }
}
