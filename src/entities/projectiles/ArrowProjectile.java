package entities.projectiles;

import entities.DamageType;
import entities.Entity;
import main.GameContext;
import org.joml.Vector2f;

public abstract class ArrowProjectile extends SimpleProjectile {
    private final int damage;
    private final Entity sender;
    public ArrowProjectile(Entity sender, Vector2f direction, int damage) {
        super(new Vector2f(sender.getX(), sender.getY()), direction);
        this.sender = sender;
        this.damage = damage;
    }
    public void damage(Entity entity, GameContext context) {
        if (entity == sender) return;
        dead = true;
        entity.damage(damage, DamageType.NORMAL, context);
    }
}
