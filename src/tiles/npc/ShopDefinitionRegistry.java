package tiles.npc;

import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.core.Texture;
import main.ItemConstants;

import java.util.HashMap;

public class ShopDefinitionRegistry {
    private static final HashMap<String, ShopNpcDefinition> defs = new HashMap<>();
    public static ShopNpcDefinition get(String key) {
        return defs.get(key);
    }
    public static void register(String key, ShopNpcDefinition def) {
        defs.put(key, def);
    }
    public static void register(ShopNpcDefinition def) {
        defs.put(def.id(), def);
    }
}
