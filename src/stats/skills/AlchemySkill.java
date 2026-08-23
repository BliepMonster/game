package stats.skills;

import saving.SavedSkill;
import stats.StatHolder;

public class AlchemySkill extends SimpleSkill {
    public AlchemySkill() {
        super(25, 1.6f, 10, "Alchemy");
    }
    public AlchemySkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.MAGIC_POWER, getLevel()*2f).with(StatHolder.Stat.DEFENSE, (float) Math.log(getLevel()+1));
    }
}
