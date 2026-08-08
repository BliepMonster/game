package combat.abilities;

import java.util.HashMap;

public class AbilityRegistry {
    public static final HashMap<String, ItemAbility> registry = new HashMap<>();
    static {
        registry.put("copper_hoe", new HoeHarvestAbility(2));
        registry.put("iron_hoe", new HoeHarvestAbility(3));
        registry.put("steel_hoe", new HoeHarvestAbility(5));
        registry.put("crafting_station", new CraftingTablePlacerAbility());
    }
}
