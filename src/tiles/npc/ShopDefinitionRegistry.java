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
    static {
        register("void", new ShopNpcDefinition(
                "Void",
                "void",
                new Texture("res/npc_test.png"),
                1.5f,
                new ShopEntry(
                        new PreCraftRequirement[]{},
                        new ItemRequirement[]{},
                        new MoneyRequirement(Currency.COINS, 50),
                        new ResourceGiver(ItemConstants.ITEM_CARROT, 1),
                        new Texture("res/bred.png")
                )
        ));
    }
}
