package crafting;

import main.GameContext;
import stats.inventory.items.Item;
import stats.skills.SkillSet;

public class ItemCraftingRecipe implements CraftingRecipe {
    public final ItemRequirement[] ingredients;
    private final CraftingResult result;
    public final PreCraftRequirement[] preCraftRequirements;
    public final String name;
    public final SkillSet.SkillType skill;
    public final int xp;
    public ItemCraftingRecipe(ItemRequirement[] ingredients, CraftingResult result, PreCraftRequirement[] preCraftRequirements, String name, SkillSet.SkillType skill, int xp) {
        this.ingredients = ingredients;
        this.result = result;
        this.preCraftRequirements = preCraftRequirements;
        this.name = name;
        this.skill = skill;
        this.xp = xp;
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
            consume(storage);
            produce(storage);
        }
        if (skill != null)
            context.getPlayer().skills.get(skill).getXP(xp);
    }
    public void consume(FullItemStorage storage) {
        for (ItemRequirement requirement : ingredients) {
            requirement.consume(storage);
        }
    }
    public void produce(FullItemStorage storage) {
        result.produce(storage);
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
