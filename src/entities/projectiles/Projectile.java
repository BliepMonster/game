package entities.projectiles;

import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.Entity;
import main.GameContext;
import main.Camera;
import org.joml.Vector2f;

public abstract class Projectile implements Entity {
    protected final Vector2f position;
    private float prevX, prevY;
    private GameContext context;
    protected boolean dead = false;
    public Projectile(Vector2f position) {
        this.position = position;
        this.prevX = position.x;
        this.prevY = position.y;
    }
    public abstract void damage(Entity entity, GameContext context);
    public abstract void moveSelf(Hitbox world);
    public void move(Hitbox world) {
        prevX = position.x;
        prevY = position.y;
        moveSelf(world);
        if (getHitbox().collidesWith(world)) dead = true;
    }
    @Override
    public float getRenderX() {
        return prevX + (position.x - prevX) * getCameraAlpha();
    }
    @Override
    public float getRenderY() {
        return prevY + (position.y - prevY) * getCameraAlpha();
    }
    private float getCameraAlpha() {
        return context == null ? 0f : context.getCamera().getInterpolationAlpha();
    }
    public void tick(GameContext context) {
        this.context = context;
        var entities = context.getEntities();
        var hitbox = getHitbox();
        for (Entity entity : entities) {
            if (entity.isAttackable() && entity.getHitbox().collidesWith(hitbox))
                damage(entity, context);
        }
        var player = context.getPlayer();
        if (player.getHitbox().collidesWith(hitbox)) damage(player, context);
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public boolean shouldDie(GameContext context) {
        return dead;
    }
    public void damage(int damage, DamageType type, GameContext context) {}
    public boolean isAttackable() {
        return false;
    }
    public void knock(Vector2f vec, float dist) {}
}
