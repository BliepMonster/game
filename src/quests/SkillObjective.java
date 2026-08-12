package quests;

import main.GameContext;
import stats.skills.SkillSet;

public class SkillObjective extends Objective {
    private final SkillSet.SkillType skill;
    private final int level;
    private final String name;

    public SkillObjective(SkillSet.SkillType skill, int level, String name) {
        this.skill = skill;
        this.level = level;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return context.getPlayer().skills.get(skill).getLevel() >= level;
    }

    @Override
    public int getProgress(GameContext context) {
        return Math.min(context.getPlayer().skills.get(skill).getLevel(), level);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return level;
    }

    @Override
    public String getType() {
        return "skill";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String s = skill.name();
        return "Reach level " + level + " in " + s.charAt(0) + s.substring(1).toLowerCase();
    }
}
