package tiles.resources;

import entities.LootTable;
import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;

public record HittableResourceDescription(int respawnTicks, String itemID, int yield, SkillSet.SkillType skill, int xp, StatHolder.Stat modifier, int health, int levelRequirement, boolean standable, ToolItemDefinition.PowerType powerType, LootTable table, float meshH) {
    public HittableResourceDescription(int respawnTicks, String itemID, int yield, SkillSet.SkillType skill, int xp, StatHolder.Stat modifier, int health, int levelRequirement, boolean standable, ToolItemDefinition.PowerType powerType) {
        this(respawnTicks, itemID, yield, skill, xp, modifier, health, levelRequirement, standable, powerType, new LootTable(), 1);
    }
}
