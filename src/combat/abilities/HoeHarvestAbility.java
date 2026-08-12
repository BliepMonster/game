package combat.abilities;

import main.GameContext;
import org.joml.Vector2f;
import org.joml.Vector2i;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;
import tiles.resources.CropTilePart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoeHarvestAbility extends ItemAbility {
    private final int count;
    public static final int RADIUS = 3; // allows 49 total harvests for a cheap algorithm

    public HoeHarvestAbility(int count) {
        this.count = count;
    }

    List<Tile> getTilesInRing(GameContext context, Vector2i position, int minR, int maxR) {
        List<Tile> result = new ArrayList<>();

        int ox = position.x;
        int oy = position.y;

        for (int dx = -maxR; dx <= maxR; dx++) {
            for (int dy = -maxR; dy <= maxR; dy++) {

                int dist = Math.abs(dx) + Math.abs(dy); // Manhattan distance

                if (dist < minR || dist > maxR) continue;

                Tile t = context.getTile(ox + dx, oy + dy);
                if (t != null) result.add(t);
            }
        }

        return result;
    }

    @Override
    public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
        int available = this.count;
        var player = context.getPlayer();
        worldClickLocation = player.getFacingPosition(worldClickLocation).add(.5f, .5f);
        if (available == 0) return new ItemUseResult(Optional.of(item), false);
        var vec = new Vector2i((int) Math.floor(worldClickLocation.x), (int) Math.floor(worldClickLocation.y));
        loop:
        for (int i = 0; i <= RADIUS; ++i) {
            var tiles = getTilesInRing(context, vec, i, i);
            for (Tile tile1 : tiles) {
                var part = tile1.getPart();
                if (part == null) continue;
                if (part instanceof CropTilePart crop && crop.isReady()) {
                    crop.harvestWithPenalty(context, Math.min((RADIUS-i+1)/(float) RADIUS, 1));
                    available--;
                    if (available == 0) break loop;
                }
            }
        }
        return new ItemUseResult(Optional.of(item), true);
    }
}
