package combat.melee;

import combat.equipment.EquipmentSlot;
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
        if (ticks-lastUse < cooldownTicks) return new ItemUseResult(Optional.of(item), false);
        lastUse = ticks;
        var entities = context.getEntities();
        var stats = context.getPlayer().stats;
        for (var entity : entities) {
            if (entity.isAttackable() && canHit(entity, worldPos, context)) {
                var cc = stats.get(StatHolder.Stat.CRIT_CHANCE);
                var cd = stats.get(StatHolder.Stat.CRIT_DAMAGE)/100;
                var chance = cc % 100;
                var mul = (cc-chance)/100;
                float ccount = mul;
                if (Math.random() < chance/100) ccount++;
                var dmg = (int) ((stats.get(StatHolder.Stat.DAMAGE)*stats.get(StatHolder.Stat.STRENGTH)/100)*(ccount == 0 ? 1 : ccount*cd));
                entity.damage(dmg, type, context);
                float angle = (float)Math.atan2(worldPos.y - context.getPlayer().getY(), worldPos.x - context.getPlayer().getX());
                angle += (float) ((Math.random() - 0.5f) * 0.8f);
                Vector2f result = new Vector2f(
                        (float)Math.cos(angle),
                        (float)Math.sin(angle)
                );
                entity.knock(result, stats.get(StatHolder.Stat.KNOCKBACK)*(float)(Math.random()/5+0.9));
            }
        }
        return new ItemUseResult(Optional.of(item), true);
    }

    @Override
    public int getCooldownTime() {
        return cooldownTicks;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.NONE;
    }

    @Override
    public StatHolder getWornStats(Item item, GameContext context) {
        return new StatHolder();
    }
    public abstract StatHolder getSelfStats(Item item, GameContext context);
    public StatHolder getHeldStats(Item item, GameContext context) {
        return getSelfStats(item, context).with(StatHolder.Stat.DAMAGE, damage).with(StatHolder.Stat.KNOCKBACK, knockback);
    }

    @Override
    public boolean canApplyScrolls() {
        return true;
    }
}
