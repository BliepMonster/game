package engine.hitboxes;

import java.io.Serializable;

public class CircularHitbox implements Hitbox, Serializable {
    protected final Positioned owner;
    protected final float originX, originY;
    protected final float radius;

    public CircularHitbox(Positioned owner, float originX, float originY, float radius) {
        this.owner = owner;
        this.originX = originX;
        this.originY = originY;
        this.radius = radius;
    }

    public float getCenterX() {
        return owner.getX() + originX;
    }

    public float getCenterY() {
        return owner.getY() + originY;
    }

    public float getRadius() {
        return radius;
    }

    public boolean collidesWith(Hitbox hitbox) {
        switch (hitbox) {
            case null -> {
                return false;
            }
            case CircularHitbox circular -> {
                return circleIntersectsCircle(circular);
            }
            case RectangularHitbox rectangular -> {
                return circleIntersectsRect(rectangular);
            }
            case CompoundHitbox compound -> {
                for (Hitbox part : compound.parts) {
                    if (collidesWith(part)) {
                        return true;
                    }
                }
            }
            default -> {
                return hitbox.collidesWith(this);
            }
        }
        return false;
    }

    private boolean circleIntersectsCircle(CircularHitbox other) {
        float dx = getCenterX() - other.getCenterX();
        float dy = getCenterY() - other.getCenterY();
        float distSq = dx * dx + dy * dy;
        float radiiSum = radius + other.radius;
        return distSq <= radiiSum * radiiSum;
    }

    private boolean circleIntersectsRect(RectangularHitbox r) {
        float rx = r.getX() + r.owner.getX();
        float ry = r.getY() + r.owner.getY();
        float minX = Math.min(rx, rx + r.getWidth());
        float maxX = Math.max(rx, rx + r.getWidth());
        float minY = Math.min(ry, ry + r.getHeight());
        float maxY = Math.max(ry, ry + r.getHeight());
        float cx = getCenterX();
        float cy = getCenterY();

        float closestX = Math.max(minX, Math.min(cx, maxX));
        float closestY = Math.max(minY, Math.min(cy, maxY));
        float dx = cx - closestX;
        float dy = cy - closestY;
        return dx * dx + dy * dy <= radius * radius;
    }
}
