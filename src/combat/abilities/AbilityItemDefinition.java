package combat.abilities;

import combat.equipment.EquipmentSlot;
import engine.core.Texture;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

import java.util.Optional;

public class AbilityItemDefinition extends ItemDefinition {
    private final StatHolder stats;
    private final int cooldownTicks;

    private final ItemAbility ability;
    public AbilityItemDefinition(String id, String name, String description, Texture texture, StatHolder stats, int cooldownTicks, ItemAbility ability) {
        super(id, name, description, texture);
        this.stats = stats;
        this.cooldownTicks = cooldownTicks;
        this.ability = ability;
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        if (Game.getTickCount() - cooldownTicks > lastUse) {
            lastUse = Game.getTickCount();
            return ability.use(tile, item, worldPos, context);
        }
        return new ItemUseResult(Optional.of(item), false);
    }

    @Override
    public StatHolder getHeldStats(Item item, GameContext context) {
        return stats.clone();
    }

    @Override
    public StatHolder getWornStats(Item item, GameContext context) {
        return stats.clone();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.NONE;
    }

    @Override
    public boolean canApplyScrolls() {
        return true;
    }
}
