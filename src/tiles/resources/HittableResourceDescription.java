package tiles.resources;

import stats.StatHolder;
import stats.inventory.items.ToolItemDefinition;
import stats.skills.SkillSet;

public record HittableResourceDescription(int respawnTicks, String itemID, int yield, SkillSet.SkillType skill, int xp, StatHolder.Stat modifier, int health, int levelRequirement, boolean standable, ToolItemDefinition.PowerType powerType) {}
