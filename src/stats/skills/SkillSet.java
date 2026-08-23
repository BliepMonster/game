package stats.skills;

import saving.SavedSkill;
import saving.SavedSkillSet;
import stats.StatHolder;

import static stats.skills.SkillSet.SkillType.*;

public class SkillSet {
    public enum SkillType {
        FORAGING, FARMING, COMBAT, MINING, ALCHEMY
    }
    public static final int NUM_SKILLS = SkillType.values().length;
    public static final int SKILL_FORAGING = FORAGING.ordinal();
    public static final int SKILL_FARMING = FARMING.ordinal();
    public static final int SKILL_COMBAT = COMBAT.ordinal();
    public static final int SKILL_MINING = MINING.ordinal();
    public static final int SKILL_ALCHEMY = ALCHEMY.ordinal();
    private final Skill[] skills = new Skill[NUM_SKILLS];
    public SkillSet(SavedSkillSet set) {
        SavedSkill[] saved = set.skills();
        skills[SKILL_FORAGING] = new ForagingSkill(saved[FORAGING.ordinal()]);
        skills[SKILL_FARMING] = new FarmingSkill(saved[FARMING.ordinal()]);
        skills[SKILL_COMBAT] = new CombatSkill(saved[COMBAT.ordinal()]);
        skills[SKILL_MINING] = new MiningSkill(saved[MINING.ordinal()]);
        // saves from before ALCHEMY existed have no entry for it
        skills[SKILL_ALCHEMY] = saved.length > SKILL_ALCHEMY ? new AlchemySkill(saved[SKILL_ALCHEMY]) : new AlchemySkill();
    }
    public SkillSet() {
        skills[SKILL_FORAGING] = new ForagingSkill();
        skills[SKILL_FARMING] = new FarmingSkill();
        skills[SKILL_COMBAT] = new CombatSkill();
        skills[SKILL_MINING] = new MiningSkill();
        skills[SKILL_ALCHEMY] = new AlchemySkill();
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
