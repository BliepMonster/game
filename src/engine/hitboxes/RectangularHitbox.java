package engine.hitboxes;

import java.io.Serializable;

public class RectangularHitbox implements Hitbox, Serializable {
    protected float x, y, width, height;
    protected Positioned owner;

    public RectangularHitbox(float x, float y, float width, float height, Positioned movingTarget) {
        this.x = x;
        this.width = width;
        this.y = y;
        this.height = height;
        this.owner = movingTarget;
    } public boolean collidesWith(Hitbox h) {
        if (h == this) return true;
        switch (h) {
            case null -> {
                return false;
            }
            case RectangularHitbox hitbox -> {
                return x + owner.getX() < hitbox.x + hitbox.owner.getX() + hitbox.width &&
                        x + owner.getX() + width > hitbox.x + hitbox.owner.getX() &&
                        y + owner.getY() < hitbox.y + hitbox.owner.getY() + hitbox.height &&
                        y + owner.getY() + height > hitbox.y + hitbox.owner.getY();
            }
            case CompoundHitbox hitbox -> {
                for (Hitbox part : hitbox.parts) {
                    if (collidesWith(part)) {
                        return true;
                    }
                }
            }
            default -> {
            }
        }
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}