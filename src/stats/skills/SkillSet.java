package stats.skills;

import stats.StatHolder;

import static stats.skills.SkillSet.SkillType.*;

public class SkillSet {
    public enum SkillType {
        FORAGING, FARMING, COMBAT, MINING
    }
    public static final int NUM_SKILLS = SkillType.values().length;
    public static final int SKILL_FORAGING = FORAGING.ordinal();
    public static final int SKILL_FARMING = FARMING.ordinal();
    public static final int SKILL_COMBAT = COMBAT.ordinal();
    public static final int SKILL_MINING = MINING.ordinal();
    private final Skill[] skills = new Skill[NUM_SKILLS];
    public SkillSet() {
        skills[SKILL_FORAGING] = new ForagingSkill();
        skills[SKILL_FARMING] = new FarmingSkill();
        skills[SKILL_COMBAT] = new CombatSkill();
        skills[SKILL_MINING] = new MiningSkill();
    }
    public void giveXP(SkillType skill, int count) {
        skills[skill.ordinal()].getXP(count);
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
    public Skill get(SkillType type) {
        return skills[type.ordinal()];
    }
}
