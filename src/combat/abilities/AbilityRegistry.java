package combat.abilities;

import java.util.HashMap;

public class AbilityRegistry {
    public static final HashMap<String, ItemAbility> registry = new HashMap<>();
    static {
        registry.put("copper_hoe", new HoeHarvestAbility(2));
        registry.put("iron_hoe", new HoeHarvestAbility(3));
        registry.put("steel_hoe", new HoeHarvestAbility(5));
        registry.put("crafting_station", new CraftingTablePlacerAbility());
        registry.put("deconstructor", new DeconstructorPlacerAbility());
        registry.put("chest", new ChestPlacerAbility());
        registry.put("furnace", new FurnacePlacerAbility());
        registry.put("crusher", new CrusherPlacerAbility());
        registry.put("scroll_table", new ScrollTablePlacerAbility());
        registry.put("polisher", new PolisherPlacerAbility());
        registry.put("garden_pot", new GardenPotPlacerAbility());
    }
}
