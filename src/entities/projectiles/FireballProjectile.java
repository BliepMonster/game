package entities.projectiles;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.DamageType;
import entities.Entity;
import entities.ExplosionEntity;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;

import java.awt.*;

public class FireballProjectile extends SimpleProjectile {
    private final int damage;
    private final Entity sender;
    public void damage(Entity entity, GameContext context) {
        if (entity == sender) return;
        this.dead = true;
        entity.damage(damage, DamageType.FIRE, context);
        entity.knock(this.movement.normalize(new Vector2f()), 5f);
    }
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.rect(Color.red, position.x-.25f, position.y-.25f, .5f, .5f), -position.y+.25f);
    }
    public final Hitbox hitbox = new RectangularHitbox(-.25f, -.25f, .5f, .5f, this);

    @Override
    public Hitbox getHitbox() {
        return hitbox;
    }
    public float getWidth() {
        return .5f;
    }
    public float getHeight() {
        return .5f;
    }

    public FireballProjectile(Vector2f position, Vector2f movement, int damage, Entity sender) {
        super(position, movement);
        this.damage = damage;
        this.sender = sender;
    }

    @Override
    public void die(GameContext context) {
        context.spawn(new ExplosionEntity(
                (int)(120*context.getPlayer().stats.get(StatHolder.Stat.MAGIC_POWER)/100f),
                4, Game.getTickCount(), 4, this.position, context
        ));
    }
}
