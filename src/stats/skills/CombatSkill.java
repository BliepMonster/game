package stats.skills;

import saving.SavedSkill;
import stats.StatHolder;

public class CombatSkill extends SimpleSkill {
    public CombatSkill() {
        super(25, 1.6f, 10, "Combat");
    }
    public CombatSkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.STRENGTH, 4*getLevel()).with(StatHolder.Stat.CRIT_CHANCE, getLevel()).with(StatHolder.Stat.KNOCKBACK, (float) Math.log(getLevel()+1)/30);
    }
}
