package stats.skills;

import stats.StatHolder;

public class CombatSkill extends SimpleSkill {
    public CombatSkill() {
        super(25, 2, 10, "Combat");
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.STRENGTH, 4*getLevel());
    }
}
