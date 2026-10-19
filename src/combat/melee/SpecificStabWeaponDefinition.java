package combat.melee;

import engine.core.textures.Texture;
import engine.hitboxes.RectangularHitbox;
import engine.hitboxes.Unpositioned;
import entities.DamageType;
import entities.Entity;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;

public class SpecificStabWeaponDefinition extends MeleeWeaponItemDefinition {
    private final StatHolder stats;
    public SpecificStabWeaponDefinition(float knockback, int damage, DamageType type, int cooldownTicks, String name, String description, String id, Texture texture, StatHolder stats) {
        super(knockback, damage, type, cooldownTicks, name, description, id, texture);
        this.stats = stats;
    }
    public boolean canHit(Entity entity, Vector2f target, GameContext context) {
        return entity.getHitbox().collidesWith(new RectangularHitbox(target.x, target.y, .01f, .01f, Unpositioned.INSTANCE));
    }

    @Override
    public StatHolder getSelfStats(Item item, GameContext context) {
        return stats.clone();
    }
}
