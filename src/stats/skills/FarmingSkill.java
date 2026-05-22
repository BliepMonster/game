package stats.skills;

import stats.StatHolder;

public class FarmingSkill extends SimpleSkill {
    public FarmingSkill() {
        super(25, 2, 10, "Farming");
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.FARMING_YIELD, 4*getLevel());
    }
}
