package combat.melee;

import engine.core.Texture;
import engine.hitboxes.ConeHitbox;
import entities.DamageType;
import entities.Entity;
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
    public ConeWeaponDefinition(float knockback, DamageType type, int damage, float size, float angleInRadians, int cooldownTicks, String name, String description, String id, Texture texture, StatHolder stats) {
        super(knockback, damage, type, cooldownTicks, name, description, id, texture);
        this.size = size;
        this.angle = angleInRadians;
        this.stats = stats;
    }
    public boolean canHit(Entity entity, Vector2f hitLocation, GameContext context) {
        var player = context.getPlayer();
        float direction = (float) Math.atan2(hitLocation.y - player.getY(), hitLocation.x - player.getX());
        return new ConeHitbox(player, 0, 0, size, direction, angle).collidesWith(entity.getHitbox());
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
