package tiles;

import crafting.*;
import crafting.ui.CraftingPanel;
import engine.core.text.Font;
import engine.gui.GUI;
import main.Camera;
import main.GameContext;
import main.StaticItemLoader;
import stats.inventory.items.BasicFoodItemDefinition;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static main.ItemConstants.*;

public class CraftingStationTilePart extends TilePart {
    public static final Color COLOR = new Color(100, 100, 0);
    private static Font FONT;
    public static final List<CraftingRecipe> RECIPES = new ArrayList<>();
    static {
        var map = new HashMap<String, Integer>();
        Supplier<Item>[] es = new Supplier[0];
        map.put(ITEM_FLOUR, 1);
        RECIPES.add(
                new ItemCraftingRecipe(
                        new ItemRequirement[]{
                                new ResourceRequirement(ITEM_WHEAT, 10)
                        },
                        new CraftingResult(
                                map,
                                es
                        ),
                        new PreCraftRequirement[]{},
                        "Flour"
                )
        );
        es = new Supplier[] {
                () -> new Item(ItemDefinition.get(StaticItemLoader.BREAD_ITEM_ID))
        };
        map = new HashMap<>();
        RECIPES.add(
                new ItemCraftingRecipe(
                        new ItemRequirement[]{
                                new ResourceRequirement(ITEM_FLOUR, 2)
                        },
                        new CraftingResult(
                                map,
                                es
                        ),
                        new PreCraftRequirement[]{},
                        "Bread"
                )
        );
    }
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
        var inv = new FullItemStorage(player.items, player.inventory);
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(
                    new CraftingPanel(
                            RECIPES,
                            inv,
                            context,
                            FONT,
                            0
                    ),
                    0
            );
        }
    }
}
