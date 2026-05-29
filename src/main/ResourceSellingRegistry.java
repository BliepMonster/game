package main;

import java.util.HashMap;

public final class ResourceSellingRegistry {
    private ResourceSellingRegistry() {}
    private static final HashMap<String, Integer> sellPrices = new HashMap<>();
    public static void register(String key, int value) {
        sellPrices.put(key, value);
    }
    public static int get(String key) {
        var v = sellPrices.get(key);
        return v == null ? -1 : v;
    }
    static {
        register(ItemConstants.ITEM_CARROT, 5);
        register(ItemConstants.ITEM_COAL, 8);
        register(ItemConstants.ITEM_COPPER_BAR, 30);
        register(ItemConstants.ITEM_COPPER_ORE, 10);
        register(ItemConstants.ITEM_STRAWBERRY, 3);
        register(ItemConstants.ITEM_MAPLE_WOOD, 2);
        register(ItemConstants.ITEM_IRON_ORE, 15);
        register(ItemConstants.ITEM_IRON_BAR, 40);
        register(ItemConstants.ITEM_STEEL_BAR, 110);
        register(ItemConstants.ITEM_WHEAT, 3);
        register(ItemConstants.ITEM_POTATO, 6);
        register(ItemConstants.ITEM_FLOUR, 35);
        register(ItemConstants.ITEM_BOWL, 12);
        register(ItemConstants.ITEM_GOLD_ORE, 55);
        register(ItemConstants.ITEM_GOLD_BAR, 130);
    }
}
