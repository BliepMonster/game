package stats.skills;

import stats.StatHolder;

public class SkillSet {
    public static final int NUM_SKILLS = 1;
    public static final int SKILL_FORAGING = 0;
    private final Skill[] skills = new Skill[NUM_SKILLS];
    public SkillSet() {
        skills[SKILL_FORAGING] = new ForagingSkill();
    }
    public void giveXP(int skill, int count) {
        skills[skill].getXP(count);
    }
    public StatHolder getStatBoost() {
        StatHolder holder = new StatHolder();
        for (Skill skill : skills) {
            holder.merge(skill.getStatBoost());
        }
        return holder;
    }
    public int size() {
        return NUM_SKILLS;
    }
    public Skill get(int index) {
        return skills[index];
    }
}
