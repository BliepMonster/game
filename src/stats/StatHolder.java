package stats;

public class StatHolder {
    public float speed;
    public StatHolder() {
        this.speed = 1;
    }
    public StatHolder withSpeed(float speed) {
        this.speed = speed;
        return this;
    }
    public StatHolder merge(StatHolder other) {
        this.speed += other.speed;
        return this;
    }
}
