package tiles.npc;

import java.util.HashMap;

public class ShopDefinitionRegistry {
    private static final HashMap<String, NpcDefinition> defs = new HashMap<>();
    public static NpcDefinition get(String key) {
        return defs.get(key);
    }
    public static void register(String key, NpcDefinition def) {
        defs.put(key, def);
    }
    public static void register(NpcDefinition def) {
        defs.put(def.id(), def);
    }
}
