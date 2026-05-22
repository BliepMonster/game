package stats.skills;

import engine.notifications.NotificationSystem;
import stats.StatHolder;

import java.awt.*;

public class ForagingSkill extends SimpleSkill {

    public ForagingSkill() {
        super(25, 2, 10, "Foraging");
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().withForagingYield(4*getLevel());
    }
}
