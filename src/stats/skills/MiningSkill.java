package stats.skills;

import saving.SavedSkill;
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
    public MiningSkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.MINING_YIELD, getLevel()*4).with(StatHolder.Stat.MINING_POWER, getLevel()).with(StatHolder.Stat.DEFENSE, (float) Math.log(getLevel()+1));
    }
}
