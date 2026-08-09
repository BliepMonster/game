package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.house.ChestTilePart;

public class ChestPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new ChestTilePart(position);
    }
}
