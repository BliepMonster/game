package stats.skills;

import engine.notifications.NotificationSystem;
import stats.StatHolder;

import java.awt.*;

public class ForagingSkill extends Skill {
    public static final int MAX = 25;
    public static final int MULTIPLIER = 2;
    private int level = 0;
    private int xp = 0;
    private int needed = 10;

    @Override
    public StatHolder getStatBoost() {
        return new StatHolder().withForagingYield(4*level);
    }
    public String getName() {
        return "Foraging";
    }
    public int getLevel() {
        return level;
    }
    public void getXP(int xp) {
        this.xp += xp;
        while (this.xp >= needed && level != MAX) {
            level++;
            this.xp -= needed;
            needed *= MULTIPLIER;
            NotificationSystem.add("Foraging Level Up! "+level, Color.green);
        }
    }
    public boolean isAtMax() {
        return level == MAX;
    }
    public int getSubLevelXP() {
        return xp;
    }
    public int getRequiredXP() {
        return needed;
    }
}
