package stats;

import java.util.Arrays;

public class StatHolder implements Cloneable {
    public StatHolder() {
        Arrays.fill(multiplicative, 1);
    }
    @Override
    public StatHolder clone() {
        try {
            StatHolder holder = (StatHolder) super.clone();
            holder.stats = new float[NUM_STATS];
            System.arraycopy(this.stats, 0, holder.stats, 0, NUM_STATS);
            holder.additive = new float[NUM_STATS];
            System.arraycopy(this.additive, 0, holder.additive, 0, NUM_STATS);
            holder.multiplicative = new float[NUM_STATS];
            System.arraycopy(this.multiplicative, 0, holder.multiplicative, 0, NUM_STATS);
            return holder;
        } catch (CloneNotSupportedException e) {
            var holder = new StatHolder();
            System.arraycopy(this.stats, 0, holder.stats, 0, NUM_STATS);
            holder.additive = new float[NUM_STATS];
            System.arraycopy(this.additive, 0, holder.additive, 0, NUM_STATS);
            holder.multiplicative = new float[NUM_STATS];
            System.arraycopy(this.multiplicative, 0, holder.multiplicative, 0, NUM_STATS);
            return holder;
        }
    }

    public boolean isEmpty() {
        for (float stat : stats) if (stat != 0) return false;
        for (float stat : additive) if (stat != 0) return false;
        for (float stat : multiplicative) if (stat != 1) return false;
        return true;
    }

    public enum Stat {
        SPEED("Speed", true),
        FORAGING_YIELD("Foraging Yield", true),
        FARMING_YIELD("Farming Yield", true),
        MAX_HEALTH("Health", true),
        STRENGTH("Strength", true),
        MINING_YIELD("Mining Yield", true),
        DAMAGE("Damage", true),
        MINING_POWER("Mining Power", true),
        FORAGING_POWER("Foraging Power", true),
        KNOCKBACK("Knockback", true),
        DEFENSE("Defense", true),
        CRIT_CHANCE("Crit Chance", true),
        CRIT_DAMAGE("Crit Damage", true),
        REGENERATION("Regeneration", true),
        GLOWING("Glowing", true),
        MAGIC_POWER("Magic Power", true),
        LUCK("Luck", true),
        HEALING("Healing", false)
        ;
        public final String name;
        public final boolean visible;
        Stat(String s, boolean visible) {
            this.name = s;
            this.visible = visible;
        }
    }
    public static final int NUM_STATS = Stat.values().length;
    public static final int SPEED_INDEX = Stat.SPEED.ordinal();
    public static final int FORAGING_YIELD_INDEX = Stat.FORAGING_YIELD.ordinal();
    private float[] stats = new float[NUM_STATS];
    private float[] additive = new float[NUM_STATS];
    private float[] multiplicative = new float[NUM_STATS];
    public StatHolder withForagingYield(float yield) {
        this.stats[FORAGING_YIELD_INDEX] = yield;
        return this;
    }
    public float get(Stat stat) {
        return stats[stat.ordinal()]*(1+additive[stat.ordinal()])*multiplicative[stat.ordinal()];
    }
    public float getBase(Stat stat) {
        return stats[stat.ordinal()];
    }
    public float getAdditive(Stat stat) {
        return additive[stat.ordinal()];
    }
    public float getMultiplicative(Stat stat) {
        return multiplicative[stat.ordinal()];
    }
    public StatHolder with(Stat stat, float v) {
        this.stats[stat.ordinal()] = v;
        return this;
    }
    public StatHolder withAdditive(Stat stat, float v) {
        this.additive[stat.ordinal()] = v;
        return this;
    }
    public StatHolder withMultiplicative(Stat stat, float v) {
        this.multiplicative[stat.ordinal()] = v;
        return this;
    }
    public StatHolder merge(StatHolder other) {
        for (int i = 0; i < stats.length; i++) {
            stats[i] += other.stats[i];
        }
        for (int i = 0; i < additive.length; i++) {
            additive[i] += other.additive[i];
        }
        for (int i = 0; i < multiplicative.length; i++) {
            multiplicative[i] *= other.multiplicative[i];
        }
        return this;
    }
}
