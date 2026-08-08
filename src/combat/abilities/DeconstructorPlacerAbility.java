package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.DeconstructorTilePart;

public class DeconstructorPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new DeconstructorTilePart(position);
    }
}
