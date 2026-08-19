package stats;

public class StatHolder implements Cloneable {
    @Override
    public StatHolder clone() {
        try {
            StatHolder holder = (StatHolder) super.clone();
            holder.stats = new float[NUM_STATS];
            System.arraycopy(this.stats, 0, holder.stats, 0, NUM_STATS);
            return holder;
        } catch (CloneNotSupportedException e) {
            var holder = new StatHolder();
            System.arraycopy(this.stats, 0, holder.stats, 0, NUM_STATS);
            return holder;
        }
    }

    public boolean isEmpty() {
        for (float stat : stats) if (stat != 0) return false;
        return true;
    }

    public enum Stat {
        SPEED("Speed"),
        FORAGING_YIELD("Foraging Yield"),
        FARMING_YIELD("Farming Yield"),
        MAX_HEALTH("Health"),
        STRENGTH("Strength"),
        MINING_YIELD("Mining Yield"),
        DAMAGE("Damage"),
        MINING_POWER("Mining Power"),
        FORAGING_POWER("Foraging Power"),
        KNOCKBACK("Knockback"),
        DEFENSE("Defense"),
        CRIT_CHANCE("Crit Chance"),
        CRIT_DAMAGE("Crit Damage"),
        REGENERATION("Regeneration"),
        GLOWING("Glowing"),
        MAGIC_POWER("Magic Power"),
        ;
        public final String name;
        Stat(String s) {
            this.name = s;
        }
    }
    public static final int NUM_STATS = Stat.values().length;
    public static final int SPEED_INDEX = Stat.SPEED.ordinal();
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
