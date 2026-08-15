package crafting;

import main.GameContext;
import stats.inventory.items.Item;

public class ItemCraftingRecipe implements CraftingRecipe {
    public final ItemRequirement[] ingredients;
    private final CraftingResult result;
    public final PreCraftRequirement[] preCraftRequirements;
    public final String name;
    public ItemCraftingRecipe(ItemRequirement[] ingredients, CraftingResult result, PreCraftRequirement[] preCraftRequirements, String name) {
        this.ingredients = ingredients;
        this.result = result;
        this.preCraftRequirements = preCraftRequirements;
        this.name = name;
    }
    public boolean canCraft(FullItemStorage storage, GameContext context) {
        for (PreCraftRequirement requirement : preCraftRequirements) {
            if (!requirement.isMet(context)) return false;
        }
        for (ItemRequirement requirement : ingredients) {
            if (!requirement.isMet(storage)) return false;
        }
        return result.canCraft(storage);
    }
    public void craft(FullItemStorage storage, GameContext context) {
        // double check for safety
        if (canCraft(storage, context)) {
            for (ItemRequirement requirement : ingredients) {
                requirement.consume(storage);
            }
            result.produce(storage);
        }
    }
    public ItemRequirement[] getRequirements() {
        return ingredients;
    }
    public PreCraftRequirement[] getPreCraftRequirements() {
        return preCraftRequirements;
    }
    public String getName() {
        return name;
    }
    public Item getResultItem() {
        if (result.items.length != 1) return null;
        return result.items[0].get();
    }
}
