package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.textures.Texture;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import tiles.Tile;

import java.util.Optional;

public class BasicFoodItemDefinition extends ItemDefinition {
    private final int healthRestored;
    public BasicFoodItemDefinition(String id, String name, String description, Texture texture, int healthRestored) {
        super(id, name, description, texture);
        this.healthRestored = healthRestored;
    }
    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        var player = context.getPlayer();
        if (player.isAtFullHealth())
            return new ItemUseResult(Optional.of(item), false);
        player.heal(healthRestored);
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

    @Override
    public boolean canApplyScrolls() {
        return false;
    }
}
