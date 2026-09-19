package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.Texture;
import entities.DeployableEntity;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import tiles.Tile;

import java.util.Optional;

public class DeployableItemDefinition extends ItemDefinition {
    public final int radius;
    public final int manaCost;
    public final int duration;
    public final StatHolder buffs;
    public final Texture deployableTexture;

    public DeployableItemDefinition(String id, String name, String description, Texture texture, int duration, Texture dept, int radius, StatHolder buffs, int manaCost) {
        super(id, name, description, texture);
        this.radius = radius;
        this.buffs = buffs;
        this.duration = duration;
        this.deployableTexture = dept;
        this.manaCost = manaCost;
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        var player = context.getPlayer();
        if (tile != null && !tile.isStandable()) return new ItemUseResult(Optional.of(item), false);
        if (!player.useMana(manaCost)) return new ItemUseResult(Optional.of(item), false);
        var facing = player.getFacingPosition(worldPos);
        context.spawn(new DeployableEntity(new Vector2f((float) Math.floor(facing.x), (float) Math.floor(facing.y)), this));
        return new ItemUseResult(Optional.of(item), true);
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
