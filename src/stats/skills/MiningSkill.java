package stats.skills;

import stats.StatHolder;

public class MiningSkill extends SimpleSkill {
    public MiningSkill() {
        super(
                25,
                1.6f,
                10,
                "Mining"
        );
    }
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.MINING_YIELD, getLevel()*4).with(StatHolder.Stat.MINING_POWER, getLevel()).with(StatHolder.Stat.DEFENSE, (float) Math.log(getLevel()+1));
    }
}
