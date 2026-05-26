package crafting;

import main.GameContext;
import stats.skills.SkillSet;

public class SkillRequirement extends PreCraftRequirement {
    public final SkillSet.SkillType type;
    final String rname;
    public final int level;
    public SkillRequirement(SkillSet.SkillType type, int level) {
        this.type = type;
        this.level = level;
        var s = type.toString().toLowerCase();
        var c = s.charAt(0);
        this.rname = (Character.toString(c).toUpperCase()+s.substring(1))+" "+level;
    }

    @Override
    public String getRequirementName() {
        return rname;
    }

    @Override
    public boolean isMet(GameContext context) {
        return context.getPlayer().skills.get(type).getLevel() >= level;
    }
}
