package entities.projectiles;

import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.Entity;
import main.GameContext;
import org.joml.Vector2f;

public abstract class Projectile implements Entity {
    protected final Vector2f position;
    protected boolean dead = false;
    public Projectile(Vector2f position) {
        this.position = position;
    }
    public abstract void damage(Entity entity, GameContext context);
    public abstract void moveSelf(Hitbox world);
    public void move(Hitbox world) {
        moveSelf(world);
        if (getHitbox().collidesWith(world)) dead = true;
    }
    public void tick(GameContext context) {
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
