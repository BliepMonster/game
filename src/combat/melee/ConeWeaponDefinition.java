package combat.melee;

import engine.core.Texture;
import entities.DamageType;
import entities.Entity;
import entities.Player;
import main.GameContext;
import org.joml.Vector2f;

public class ConeWeaponDefinition extends MeleeWeaponItemDefinition {
    public final float size;
    public final float angle;
    public static final Vector2f CACHE = new Vector2f(), CACHE2 = new Vector2f();
    public ConeWeaponDefinition(DamageType type, int damage, float size, float angleInRadians, int cooldownTicks, String name, String description, String id, Texture texture) {
        super(damage, type, cooldownTicks, name, description, id, texture);
        this.size = size;
        this.angle = angleInRadians;
    }
    public ConeWeaponDefinition(float knockback, DamageType type, int damage, float size, float angleInRadians, int cooldownTicks, String name, String description, String id, Texture texture) {
        super(knockback, damage, type, cooldownTicks, name, description, id, texture);
        this.size = size;
        this.angle = angleInRadians;
    }
    private boolean canHit(Vector2f entity, Vector2f direction, Player player) {
        var vectorToEntity = new Vector2f(entity.x - player.getX(), entity.y - player.getY());
        var distanceToEntity = vectorToEntity.length();
        var angle = vectorToEntity.angle(direction);
        return angle <= this.angle && distanceToEntity <= size;
    }
    public boolean canHit(Entity entity, Vector2f hitLocation, GameContext context) {
        var player = context.getPlayer();
        var direction = CACHE2.set(hitLocation.x - player.getX(), hitLocation.y - player.getY());
        return canHit(CACHE.set(entity.getX(), entity.getY()), direction, player)
                || canHit(CACHE.set(entity.getX()+entity.getWidth(), entity.getY()), direction, player)
                || canHit(CACHE.set(entity.getX(), entity.getY()+entity.getHeight()), direction, player)
                || canHit(CACHE.set(entity.getX()+entity.getWidth(), entity.getY()+entity.getHeight()), direction, player);
    }
}
