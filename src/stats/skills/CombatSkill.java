package stats.skills;

import stats.StatHolder;

public class CombatSkill extends SimpleSkill {
    public CombatSkill() {
        super(25, 1.6f, 10, "Combat");
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.STRENGTH, 4*getLevel()).with(StatHolder.Stat.DAMAGE, getLevel()).with(StatHolder.Stat.KNOCKBACK, (float) Math.log(getLevel()+1)/10);
    }
}
