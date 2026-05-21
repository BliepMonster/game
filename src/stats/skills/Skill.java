package stats.skills;

import stats.StatHolder;

public abstract class Skill {
    public abstract StatHolder getStatBoost();
    public abstract String getName();
    public abstract int getLevel();
    public abstract void getXP(int xp);
    public abstract int getSubLevelXP();
    public abstract boolean isAtMax();
    public abstract int getRequiredXP();
}
