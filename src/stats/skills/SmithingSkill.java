package stats.skills;

import saving.SavedSkill;
import stats.StatHolder;

public class SmithingSkill extends SimpleSkill {
    public SmithingSkill() {
        super(25, 1.6f, 10, "Smithing");
    }
    public SmithingSkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.DEFENSE, (float) Math.log(getLevel()+1)).with(StatHolder.Stat.CRIT_DAMAGE, getLevel());
    }
}
