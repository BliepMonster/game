package crafting;

import main.GameContext;

public interface CraftingRecipe {
    boolean canCraft(FullItemStorage storage, GameContext context);
    void craft(FullItemStorage storage, GameContext context);
    ItemRequirement[] getRequirements();
    PreCraftRequirement[] getPreCraftRequirements();
    String getName();
}
