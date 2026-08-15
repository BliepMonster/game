package engine.hitboxes;

import java.io.Serializable;

public class ConeHitbox implements Hitbox, Serializable {
    protected final Positioned owner;
    protected final float originX, originY;
    protected final float radius;
    protected final float direction;
    protected final float halfAngle;
    protected final float cosDir, sinDir, cosHalfAngle;

    public ConeHitbox(Positioned owner, float originX, float originY, float radius, float direction, float halfAngle) {
        this.owner = owner;
        this.originX = originX;
        this.originY = originY;
        this.radius = radius;
        this.direction = direction;
        this.halfAngle = halfAngle;
        cosDir = (float) Math.cos(direction);
        sinDir = (float) Math.sin(direction);
        cosHalfAngle = (float) Math.cos(halfAngle);
    }

    public float getOriginX() {
        return owner.getX() + originX;
    }

    public float getOriginY() {
        return owner.getY() + originY;
    }

    public boolean collidesWith(Hitbox hitbox) {
        switch (hitbox) {
            case null -> {
                return false;
            }
            case RectangularHitbox rectangular -> {
                return intersectsRect(rectangular);
            }
            case CompoundHitbox compound -> {
                for (Hitbox part : compound.parts) {
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

    private boolean pointInCone(float px, float py) {
        float dx = px - getOriginX();
        float dy = py - getOriginY();
        float dist2 = dx * dx + dy * dy;
        if (dist2 > radius * radius) return false;
        if (dist2 == 0) return true;
        float inv = (float) (1.0 / Math.sqrt(dist2));
        return dx * inv * cosDir + dy * inv * sinDir >= cosHalfAngle;
    }

    private boolean intersectsRect(RectangularHitbox r) {
        float rx = r.getX() + r.owner.getX();
        float ry = r.getY() + r.owner.getY();
        float minX = Math.min(rx, rx + r.getWidth());
        float maxX = Math.max(rx, rx + r.getWidth());
        float minY = Math.min(ry, ry + r.getHeight());
        float maxY = Math.max(ry, ry + r.getHeight());
        float ox = getOriginX();
        float oy = getOriginY();

        if (ox >= minX && ox <= maxX && oy >= minY && oy <= maxY) return true;

        if (pointInCone(minX, minY) || pointInCone(maxX, minY) || pointInCone(minX, maxY) || pointInCone(maxX, maxY))
            return true;

        float left = direction - halfAngle;
        float right = direction + halfAngle;
        if (segmentIntersectsRect(ox, oy, ox + (float) Math.cos(left) * radius, oy + (float) Math.sin(left) * radius, minX, minY, maxX, maxY))
            return true;
        if (segmentIntersectsRect(ox, oy, ox + (float) Math.cos(right) * radius, oy + (float) Math.sin(right) * radius, minX, minY, maxX, maxY))
            return true;

        int segments = 16;
        float prevX = ox + (float) Math.cos(left) * radius;
        float prevY = oy + (float) Math.sin(left) * radius;
        for (int i = 1; i <= segments; i++) {
            float t = left + (right - left) * i / segments;
            float curX = ox + (float) Math.cos(t) * radius;
            float curY = oy + (float) Math.sin(t) * radius;
            if (segmentIntersectsRect(prevX, prevY, curX, curY, minX, minY, maxX, maxY)) return true;
            prevX = curX;
            prevY = curY;
        }
        return false;
    }

    private boolean segmentIntersectsRect(float x1, float y1, float x2, float y2, float minX, float minY, float maxX, float maxY) {
        if (x1 >= minX && x1 <= maxX && y1 >= minY && y1 <= maxY) return true;
        if (x2 >= minX && x2 <= maxX && y2 >= minY && y2 <= maxY) return true;
        return segmentIntersectsSegment(x1, y1, x2, y2, minX, minY, maxX, minY)
                || segmentIntersectsSegment(x1, y1, x2, y2, maxX, minY, maxX, maxY)
                || segmentIntersectsSegment(x1, y1, x2, y2, maxX, maxY, minX, maxY)
                || segmentIntersectsSegment(x1, y1, x2, y2, minX, maxY, minX, minY);
    }

    private static float cross(float px, float py, float qx, float qy) {
        return px * qy - py * qx;
    }

    private static boolean onSegment(float px, float py, float ax, float ay, float bx, float by) {
        return px >= Math.min(ax, bx) && px <= Math.max(ax, bx) && py >= Math.min(ay, by) && py <= Math.max(ay, by);
    }

    private static boolean segmentIntersectsSegment(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        float d1 = cross(cx - ax, cy - ay, bx - ax, by - ay);
        float d2 = cross(dx - ax, dy - ay, bx - ax, by - ay);
        float d3 = cross(ax - cx, ay - cy, dx - cx, dy - cy);
        float d4 = cross(bx - cx, by - cy, dx - cx, dy - cy);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) return true;
        if (d1 == 0 && onSegment(cx, cy, ax, ay, bx, by)) return true;
        if (d2 == 0 && onSegment(dx, dy, ax, ay, bx, by)) return true;
        if (d3 == 0 && onSegment(ax, ay, cx, cy, dx, dy)) return true;
        if (d4 == 0 && onSegment(bx, by, cx, cy, dx, dy)) return true;
        return false;
    }
}
