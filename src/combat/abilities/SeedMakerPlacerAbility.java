package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.SeedMakerTilePart;

public class SeedMakerPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new SeedMakerTilePart(position);
    }
}
