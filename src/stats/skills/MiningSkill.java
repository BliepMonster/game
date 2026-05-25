package stats.skills;

import stats.StatHolder;

public class MiningSkill extends SimpleSkill {
    public MiningSkill() {
        super(
                25,
                1.2f,
                10,
                "Mining"
        );
    }
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.MINING_YIELD, getLevel()*4);
    }
}
