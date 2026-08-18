package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.Texture;
import engine.core.input.Input;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import stats.StatHolder;
import tiles.Tile;
import tiles.TilePart;
import tiles.resources.HittableResourceCreator;

import java.util.Optional;

public class ToolItemDefinition extends ItemDefinition {
    public record PowerPair(PowerType type, int value) {}
    public enum PowerType {
        WOODCUTTING,
        MINING,
        ;
        StatHolder.Stat getStat() {
            return switch (this) {
                case WOODCUTTING -> StatHolder.Stat.FORAGING_POWER;
                case MINING -> StatHolder.Stat.MINING_POWER;
            };
        }
    }
    public final int cooldown;
    private final int[] strengths;
    private final StatHolder stats;
    public ToolItemDefinition(int cooldown, String id, String name, String description, Texture texture, StatHolder stats, PowerPair... pairs) {
        super(id, name, description, texture);
        this.strengths = new int[PowerType.values().length];
        for (PowerPair pair : pairs) {
            strengths[pair.type.ordinal()] = pair.value;
        }
        this.cooldown = cooldown;
        this.stats = stats;
    }
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) && context.getWorld().getID().equals("HOUSE")) {
            TilePart part = tile.getPart();
            if (part == null) return new ItemUseResult(Optional.of(item), false);
            if (part.canBreak()) {
                tile.setPart(null);
                return new ItemUseResult(Optional.of(item), true);
            }
        }
        if (Game.getTickCount() - lastUse < cooldown) {
            return new ItemUseResult(Optional.of(item), false);
        }
        lastUse = Game.getTickCount();
        if (tile == null) return new ItemUseResult(Optional.of(item), false);
        if (tile.getPart() == null) return new ItemUseResult(Optional.of(item), false);
        if (!(tile.getPart() instanceof HittableResourceCreator creator)) return new ItemUseResult(Optional.of(item), false);
        var type = creator.getPowerType();
        if (this.strengths[type.ordinal()] == 0) return new ItemUseResult(Optional.of(item), false);
        var power = context.getPlayer().stats.get(type.getStat()); // and later power buffs too
        creator.hit(context, (int) power);
        return new ItemUseResult(Optional.of(item), true);
    }

    @Override
    public int getCooldownTime() {
        return this.cooldown;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.NONE;
    }

    @Override
    public StatHolder getWornStats(Item item, GameContext context) {
        return new StatHolder();
    }

    @Override
    public StatHolder getHeldStats(Item item, GameContext context) {
        return stats.clone().with(StatHolder.Stat.MINING_POWER, strengths[PowerType.MINING.ordinal()])
                .with(StatHolder.Stat.FORAGING_POWER, strengths[PowerType.WOODCUTTING.ordinal()]);
    }

    @Override
    public boolean canApplyScrolls() {
        return true;
    }
}
