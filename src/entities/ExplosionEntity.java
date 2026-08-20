package entities;

import combat.melee.EmptyHitbox;
import engine.hitboxes.CircularHitbox;
import engine.hitboxes.Hitbox;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public class ExplosionEntity implements Entity {
    private final int damage;
    private final float kb;
    private final int tickSpawned;
    private final float radius;
    private final Vector2f position;

    public ExplosionEntity(int damage, float kb, int tickSpawned, float radius, Vector2f position, GameContext context) {
        this.damage = damage;
        this.kb = kb;
        this.tickSpawned = tickSpawned;
        this.radius = radius;
        this.position = position;
        var hitbox = new CircularHitbox(this, 0, 0, radius);
        for (var entity : context.getEntities()) {
            if (entity.isAttackable() && hitbox.collidesWith(entity.getHitbox())) {
                var v = new Vector2f(entity.getX(), entity.getY()).sub(position);
                entity.damage((int)(damage/Math.max(v.length(), 1)), DamageType.EXPLOSION, context);
                entity.knock(v.normalize(), kb);
            }
        } if (context.getPlayer().getHitbox().collidesWith(hitbox)) {
            var entity = context.getPlayer();
            var v = new Vector2f(entity.getX(), entity.getY()).sub(position);
            entity.damage((int)(damage/Math.max(v.length(), 1)), DamageType.EXPLOSION, context);
            entity.knock(v.normalize(), kb);
        }
    }

    @Override
    public void move(Hitbox world) {

    }
    public void tick(GameContext context) {

    }
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.circle(Color.orange, position.x, position.y, radius), 300);
    }
    public boolean shouldDie(GameContext context) {
        return Game.getTickCount()-3 > tickSpawned;
    }
    public Hitbox getHitbox() {
        return EmptyHitbox.INSTANCE;
    }
    public void die(GameContext context) {}

    @Override
    public void damage(int damage, DamageType type, GameContext context) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void knock(Vector2f direction, float amount) {

    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public float getX() {
        return position.x;
    }
    public float getWidth() {
        return 0;
    }
    public float getHeight() {
        return 0;
    }

}
