package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.Texture;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.StatusEffect;
import tiles.Tile;

import java.util.Optional;

public class EffectConsumableItemDefinition extends ItemDefinition {
    private final StatHolder stats;
    private final int duration;
    private final String uid;
    public EffectConsumableItemDefinition(String id, String name, String description, Texture texture, StatHolder stats, int duration, String eid) {
        super(id, name, description, texture);
        this.stats = stats;
        this.uid = eid;
        this.duration = duration;
    }
    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        var effect = new StatusEffect(uid, stats, duration);
        var player = context.getPlayer();
        if (player.effects.contains(effect)) return new ItemUseResult(Optional.of(item), false);
        player.addEffect(effect);
        return new ItemUseResult(Optional.empty(), true);
    }

    @Override
    public StatHolder getHeldStats(Item item, GameContext context) {
        return new StatHolder();
    }

    @Override
    public StatHolder getWornStats(Item item, GameContext context) {
        return new StatHolder();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.NONE;
    }
}
