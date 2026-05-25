package combat.melee;

import engine.core.Texture;
import entities.DamageType;
import entities.Entity;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

import java.util.Optional;

public abstract class MeleeWeaponItemDefinition extends ItemDefinition {
    public final DamageType type;
    public final int damage, cooldownTicks;
    private float knockback = 1;
    public float getKnockback() {
        return knockback;
    }
    public MeleeWeaponItemDefinition(int damage, DamageType type, int cooldownTicks, String name, String description, String id, Texture texture) {
        super(id, name, description, texture);
        this.damage = damage;
        this.type = type;
        this.cooldownTicks = cooldownTicks;
    }
    public MeleeWeaponItemDefinition(float kb, int damage, DamageType type, int cooldownTicks, String name, String description, String id, Texture texture) {
        super(id, name, description, texture);
        this.damage = damage;
        this.type = type;
        knockback = kb;
        this.cooldownTicks = cooldownTicks;
    }
    public abstract boolean canHit(Entity entity, Vector2f target, GameContext context);
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        int ticks = Game.getTickCount();
        if (ticks-item.lastUse < cooldownTicks) return new ItemUseResult(Optional.of(item), false);
        item.lastUse = ticks;
        var entities = context.getEntities();
        boolean used = false;
        for (var entity : entities) {
            if (entity.isAttackable() && canHit(entity, worldPos, context)) {
                entity.damage((int) (damage*context.getPlayer().stats.get(StatHolder.Stat.STRENGTH)/100), type, context);
                float angle = (float)Math.atan2(worldPos.y - context.getPlayer().getY(), worldPos.x - context.getPlayer().getX());
                angle += (float) ((Math.random() - 0.5f) * 0.8f);
                Vector2f result = new Vector2f(
                        (float)Math.cos(angle),
                        (float)Math.sin(angle)
                );
                entity.knock(result, knockback*(float)(Math.random()/5+0.9));
                used = true;
            }
        }
        return new ItemUseResult(Optional.of(item), used);
    }

    @Override
    public int getCooldownTime() {
        return cooldownTicks;
    }
}
