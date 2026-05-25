package stats.inventory.items;

import engine.core.Texture;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import tiles.Tile;
import tiles.resources.HittableResourceCreator;

import java.util.Optional;

public class ToolItemDefinition extends ItemDefinition {
    public record PowerPair(PowerType type, int value) {}
    public enum PowerType {
        WOODCUTTING,
        MINING,
    }
    public final int cooldown;
    private final int[] strengths;
    public ToolItemDefinition(int cooldown, String id, String name, String description, Texture texture, PowerPair... pairs) {
        super(id, name, description, texture);
        this.strengths = new int[PowerType.values().length];
        for (PowerPair pair : pairs) {
            strengths[pair.type.ordinal()] = pair.value;
        }
        this.cooldown = cooldown;
    }
    public ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos) {
        if (Game.getTickCount() - item.lastUse < cooldown) {
            return new ItemUseResult(Optional.of(item), false);
        }
        item.lastUse = Game.getTickCount();
        if (tile.getPart() == null) return new ItemUseResult(Optional.of(item), false);
        if (!(tile.getPart() instanceof HittableResourceCreator creator)) return new ItemUseResult(Optional.of(item), false);
        var type = creator.getPowerType();
        var power = this.strengths[type.ordinal()]; // and later power buffs too
        creator.hit(context, power);
        return new ItemUseResult(Optional.of(item), true);
    }

    @Override
    public int getCooldownTime() {
        return this.cooldown;
    }
}
