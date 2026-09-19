package engine.hitboxes;

public interface Positioned {
    float getX();
    float getY();
    default float getRenderX() {
        return getX();
    }
    default float getRenderY() {
        return getY();
    }
}
