package combat.equipment;

import engine.core.Texture;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

import java.util.Optional;

public class EquipmentItemDefinition extends ItemDefinition {
    public final EquipmentSlot slot;
    public final StatHolder stats;
    public EquipmentItemDefinition(StatHolder stats, EquipmentSlot slot, String id, String name, String description, Texture texture) {
        super(id, name, description, texture);
        this.slot = slot;
        this.stats = stats;
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        return new ItemUseResult(Optional.of(item), false);
    }
    public StatHolder getHeldStats(Item item, GameContext context) {
        return new StatHolder();
    }
    public StatHolder getWornStats(Item item, GameContext context) {
        return stats.clone();
    }
    public EquipmentSlot getEquipmentSlot() {
        return slot;
    }
}
