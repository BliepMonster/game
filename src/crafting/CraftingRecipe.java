package crafting;

import main.GameContext;
import stats.inventory.items.Item;

public interface CraftingRecipe {
    boolean canCraft(FullItemStorage storage, GameContext context);
    void craft(FullItemStorage storage, GameContext context);
    ItemRequirement[] getRequirements();
    PreCraftRequirement[] getPreCraftRequirements();
    String getName();
    Item getResultItem();
}
