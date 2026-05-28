package stats.skills;

import saving.SavedSkill;
import stats.StatHolder;

public class FarmingSkill extends SimpleSkill {
    public FarmingSkill() {
        super(25, 1.6f, 10, "Farming");
    }
    public FarmingSkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.FARMING_YIELD, 4*getLevel()).with(StatHolder.Stat.MAX_HEALTH, (float) Math.log(getLevel()+1));
    }
}
