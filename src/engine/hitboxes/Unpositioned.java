package engine.hitboxes;

public class Unpositioned implements Positioned {
    private Unpositioned() {}
    public static final Unpositioned INSTANCE = new Unpositioned();
    public float getX() {
        return 0;
    }
    public float getY() {
        return 0;
    }
}
