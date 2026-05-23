package stats.inventory.items;

import engine.core.Texture;
import main.GameContext;
import org.joml.Vector2f;
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
}
