package stats.skills;

import saving.SavedSkill;
import stats.StatHolder;

public class CookingSkill extends SimpleSkill {
    public CookingSkill() {
        super(25, 1.6f, 10, "Cooking");
    }
    public CookingSkill(SavedSkill skill) {
        this();
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().with(StatHolder.Stat.MAX_HEALTH, getLevel()).with(StatHolder.Stat.REGENERATION, (float) Math.log(getLevel()+1));
    }
}
