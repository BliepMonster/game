package stats.skills;

import engine.notifications.NotificationSystem;
import stats.StatHolder;

import java.awt.*;

public abstract class SimpleSkill extends Skill {
    private final int MAX;
    private final float MULTIPLIER;
    protected int level = 0;
    protected int xp = 0;
    protected int needed;
    private final String name;
    public SimpleSkill(int max, float multiplier, int needed, String name) {
        MAX = max;
        MULTIPLIER = multiplier;
        this.needed = needed;
        this.name = name;
    }

    @Override
    public abstract StatHolder getStatBoost();
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }
    public void getXP(int xp) {
        this.xp += xp;
        while (this.xp >= needed && level != MAX) {
            level++;
            this.xp -= needed;
            needed = (int) (needed*MULTIPLIER);
            NotificationSystem.add(name+" Level Up! "+level, Color.green);
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
