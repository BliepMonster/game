package stats;

public class StatHolder {
    public float speed;
    public float foragingYield;
    public StatHolder() {
        this.speed = 1;
    }
    public StatHolder withForagingYield(float yield) {
        this.foragingYield = yield;
        return this;
    }
    public StatHolder withSpeed(float speed) {
        this.speed = speed;
        return this;
    }
    public StatHolder merge(StatHolder other) {
        this.speed += other.speed;
        this.foragingYield += other.foragingYield;
        return this;
    }
}
