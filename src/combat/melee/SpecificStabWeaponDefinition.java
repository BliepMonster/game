package combat.melee;

import engine.core.Texture;
import engine.hitboxes.RectangularHitbox;
import engine.hitboxes.Unpositioned;
import entities.DamageType;
import entities.Entity;
import main.GameContext;
import org.joml.Vector2f;

public class SpecificStabWeaponDefinition extends MeleeWeaponItemDefinition {
    public SpecificStabWeaponDefinition(int damage, DamageType type, int cooldownTicks, String name, String description, String id, Texture texture) {
        super(damage, type, cooldownTicks, name, description, id, texture);
    }
    public boolean canHit(Entity entity, Vector2f target, GameContext context) {
        return entity.getHitbox().collidesWith(new RectangularHitbox(target.x, target.y, .01f, .01f, Unpositioned.INSTANCE));
    }
}
