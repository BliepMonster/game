package combat.melee;

import engine.core.Texture;
import entities.DamageType;
import entities.Entity;
import entities.Player;
import entities.particles.SwingConeEffect;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

public class ConeWeaponDefinition extends MeleeWeaponItemDefinition {
    private final StatHolder stats;
    public final float size;
    public final float angle;
    public static final Vector2f CACHE = new Vector2f(), CACHE2 = new Vector2f();
    public ConeWeaponDefinition(float knockback, DamageType type, int damage, float size, float angleInRadians, int cooldownTicks, String name, String description, String id, Texture texture, StatHolder stats) {
        super(knockback, damage, type, cooldownTicks, name, description, id, texture);
        this.size = size;
        this.angle = angleInRadians;
        this.stats = stats;
    }
    private boolean canHit(Vector2f entity, Vector2f direction, Player player) {
        var vectorToEntity = new Vector2f(entity.x - player.getX(), entity.y - player.getY());
        var distanceToEntity = vectorToEntity.length();
        vectorToEntity.normalize();
        var angle = vectorToEntity.angle(direction);
        return Math.abs(angle) <= this.angle && distanceToEntity <= size;
    }
    public boolean canHit(Entity entity, Vector2f hitLocation, GameContext context) {
        var player = context.getPlayer();
        var direction = CACHE2.set(hitLocation.x - player.getX(), hitLocation.y - player.getY()).normalize();
        return canHit(CACHE.set(entity.getX(), entity.getY()), direction, player)
                || canHit(CACHE.set(entity.getX()+entity.getWidth(), entity.getY()), direction, player)
                || canHit(CACHE.set(entity.getX(), entity.getY()+entity.getHeight()), direction, player)
                || canHit(CACHE.set(entity.getX()+entity.getWidth(), entity.getY()+entity.getHeight()), direction, player);
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        var v = super.use(tile, item, context, worldPos);
        if (v.handled()) {
            var player = context.getPlayer();
            context.spawn(new SwingConeEffect(new Vector2f(player.getX(), player.getY()), 6, this.size, (float)Math.atan2(worldPos.y-player.getY(), worldPos.x-player.getX()), this.angle*2));
        }
        return v;
    }

    @Override
    public StatHolder getSelfStats(Item item, GameContext context) {
        return stats.clone();
    }
}
