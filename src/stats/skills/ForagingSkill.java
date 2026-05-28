package stats.skills;

import engine.notifications.NotificationSystem;
import saving.SavedSkill;
import stats.StatHolder;

import java.awt.*;

public class ForagingSkill extends SimpleSkill {

    public ForagingSkill() {
        super(25, 1.6f, 10, "Foraging");
    }
    public ForagingSkill(SavedSkill skill) {
        super(25, 1.6f, 10, "Foraging");
        needed = (int) (10*Math.pow(1.6, skill.level()));
        level = skill.level();
        xp = skill.xp();
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().withForagingYield(4*getLevel()).with(StatHolder.Stat.FORAGING_POWER, getLevel()).with(StatHolder.Stat.STRENGTH, (float) Math.log(getLevel()+1));
    }
}
