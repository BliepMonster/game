package combat.abilities;

import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

public abstract class ItemAbility {
    public abstract ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context);
}
