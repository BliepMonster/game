package tiles.resources;

import stats.StatHolder;
import stats.skills.SkillSet;

public record ResourceDefinition(String itemID, int xp, int yield, SkillSet.SkillType skillID, StatHolder.Stat modifier, int respawnTicks) {}
