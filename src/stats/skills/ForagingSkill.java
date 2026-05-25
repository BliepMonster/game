package stats.skills;

import engine.notifications.NotificationSystem;
import stats.StatHolder;

import java.awt.*;

public class ForagingSkill extends SimpleSkill {

    public ForagingSkill() {
        super(25, 1.2f, 10, "Foraging");
    }

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().withForagingYield(4*getLevel());
    }
}
