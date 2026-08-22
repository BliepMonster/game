package main;

import engine.json.JsonNumber;
import engine.json.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

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
        register(ItemConstants.ITEM_CARROT, 3);
        register(ItemConstants.ITEM_COAL, 8);
        register(ItemConstants.ITEM_COPPER_BAR, 30);
        register(ItemConstants.ITEM_COPPER_ORE, 10);
        register(ItemConstants.ITEM_STRAWBERRY, 3);
        register(ItemConstants.ITEM_MAPLE_WOOD, 2);
        register(ItemConstants.ITEM_IRON_ORE, 15);
        register(ItemConstants.ITEM_IRON_BAR, 40);
        register(ItemConstants.ITEM_STEEL_BAR, 110);
        register(ItemConstants.ITEM_WHEAT, 2);
        register(ItemConstants.ITEM_POTATO, 4);
        register(ItemConstants.ITEM_FLOUR, 35);
        register(ItemConstants.ITEM_BOWL, 12);
        register(ItemConstants.ITEM_GOLD_ORE, 55);
        register(ItemConstants.ITEM_GOLD_BAR, 130);
        register("Zombie Fragment", 100);
        try (Stream<Path> paths = Files.walk(Path.of("res/resources"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(str -> {
                        var json = new JsonParser().parse(str);
                        for (var entry : json.values.entrySet()) {
                            register(entry.getKey(), (int)((JsonNumber) entry.getValue()).num);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
