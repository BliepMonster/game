package tiles.npc;

import java.util.HashMap;

public class ShopDefinitionRegistry {
    private static final HashMap<String, ShopNpcDefinition> defs = new HashMap<>();
    public static ShopNpcDefinition get(String key) {
        return defs.get(key);
    }
    public static void register(String key, ShopNpcDefinition def) {
        defs.put(key, def);
    }
}
