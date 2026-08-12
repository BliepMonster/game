package quests;

import main.GameContext;
import stats.skills.SkillSet;

public class SkillReward extends ObjectiveReward {
    private final SkillSet.SkillType skill;
    private final int xp;

    public SkillReward(SkillSet.SkillType skill, int xp) {
        this.skill = skill;
        this.xp = xp;
    }

    @Override
    public void grant(GameContext context) {
        context.getPlayer().skills.giveXP(skill, xp);
    }

    @Override
    public String getDescription() {
        return "+" + xp + " " + skill.name().toLowerCase() + " exp";
    }
}
