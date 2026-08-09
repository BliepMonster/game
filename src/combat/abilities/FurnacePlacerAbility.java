package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.FurnaceTilePart;

public class FurnacePlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new FurnaceTilePart(position);
    }
}
