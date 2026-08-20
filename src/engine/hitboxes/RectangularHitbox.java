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
    }     public boolean collidesWith(Hitbox h) {
        if (h == this) return true;
        switch (h) {
            case null -> {
                return false;
            }
            case CircularHitbox circular -> {
                return circleIntersectsRect(circular);
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

    private boolean circleIntersectsRect(CircularHitbox c) {
        float rx = x + owner.getX();
        float ry = y + owner.getY();
        float minX = Math.min(rx, rx + width);
        float maxX = Math.max(rx, rx + width);
        float minY = Math.min(ry, ry + height);
        float maxY = Math.max(ry, ry + height);
        float cx = c.getCenterX();
        float cy = c.getCenterY();
        float cr = c.getRadius();

        float closestX = Math.max(minX, Math.min(cx, maxX));
        float closestY = Math.max(minY, Math.min(cy, maxY));
        float dx = cx - closestX;
        float dy = cy - closestY;
        return dx * dx + dy * dy <= cr * cr;
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