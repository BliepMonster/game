package combat.abilities;

import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;
import tiles.TilePart;
import tiles.WallTile;

import java.util.Optional;

public abstract class PlacerAbility extends ItemAbility {
    @Override
    public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
        if (!context.getWorld().getID().equals("HOUSE") || tile instanceof WallTile || tile.getPart() != null) {
            return new ItemUseResult(Optional.of(item), false);
        }
        tile.setPart(getPart(new Vector2f((float) Math.floor(worldClickLocation.x), (float) Math.floor(worldClickLocation.y))));
        return new ItemUseResult(Optional.empty(), true);
    }
    public abstract TilePart getPart(Vector2f position);
}
