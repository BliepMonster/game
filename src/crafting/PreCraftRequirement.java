package crafting;

import main.GameContext;

public abstract class PreCraftRequirement {
    public abstract boolean isMet(GameContext context);
    public abstract String getRequirementName();
}