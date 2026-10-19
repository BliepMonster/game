package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.textures.Texture;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import tiles.Tile;

import java.util.Optional;

public class ScrollItemDefinition extends ItemDefinition {
    public final StatHolder.Stat stat;
    public final float scaling;

    public ScrollItemDefinition(String id, String name, String description, Texture texture, StatHolder.Stat stat, float scaling) {
        super(id, name, description, texture);
        this.stat = stat;
        this.scaling = scaling;
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        return new ItemUseResult(Optional.of(item), false);
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
