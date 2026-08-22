package combat.abilities;

import engine.hitboxes.RectangularHitbox;
import engine.hitboxes.Unpositioned;
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
        if (!context.getWorld().getID().equals("HOUSE") || tile == null || tile instanceof WallTile || tile.getPart() != null) {
            return new ItemUseResult(Optional.of(item), false);
        }
        var vec = new Vector2f((float) Math.floor(worldClickLocation.x), (float) Math.floor(worldClickLocation.y));
        var part = getPart(vec);
        if (!part.isStandable() && context.getPlayer().getHitbox().collidesWith(new RectangularHitbox(vec.x, vec.y, 1, 1, Unpositioned.INSTANCE)))
            return new ItemUseResult(Optional.of(item), false);
        tile.setPart(part);
        return new ItemUseResult(Optional.empty(), true);
    }
    public abstract TilePart getPart(Vector2f position);
}
