package main;

import engine.core.Texture;
import stats.inventory.items.BasicFoodItemDefinition;
import stats.inventory.items.ItemDefinition;

public class StaticItemLoader {
    public static final Texture BREAD_TEXTURE = new Texture("res/bred.png");
    public static final String BREAD_NAME = "Bread";
    public static final String BREAD_DESCRIPTION = "A piece of bread";
    public static final String BREAD_ITEM_ID = "base:bread";
    public static void initAll() {
        ItemDefinition.register(new BasicFoodItemDefinition(BREAD_ITEM_ID, BREAD_NAME, BREAD_DESCRIPTION, BREAD_TEXTURE, 75));
    }
}
