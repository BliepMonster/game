package stats;

public class StatHolder {
    public enum Stat {
        SPEED,
        FORAGING_YIELD,
        FARMING_YIELD,
        MAX_HEALTH,
        STRENGTH,
    }
    public static final int NUM_STATS = Stat.values().length;
    public static final int SPEED_INDEX = Stat.SPEED.ordinal();
    public static final int FARMING_YIELD_INDEX = Stat.FARMING_YIELD.ordinal();
    public static final int MAX_HEALTH_INDEX = Stat.MAX_HEALTH.ordinal();
    public static final int STRENGTH_INDEX = Stat.STRENGTH.ordinal();
    public static final int FORAGING_YIELD_INDEX = Stat.FORAGING_YIELD.ordinal();
    private float[] stats = new float[NUM_STATS];
    public StatHolder withForagingYield(float yield) {
        this.stats[FORAGING_YIELD_INDEX] = yield;
        return this;
    }
    public float get(Stat stat) {
        return stats[stat.ordinal()];
    }
    public StatHolder withSpeed(float speed) {
        this.stats[SPEED_INDEX] = speed;
        return this;
    }
    public StatHolder with(Stat stat, float v) {
        this.stats[stat.ordinal()] = v;
        return this;
    }
    public StatHolder merge(StatHolder other) {
        for (int i = 0; i < stats.length; i++) {
            stats[i] += other.stats[i];
        }
        return this;
    }
}
