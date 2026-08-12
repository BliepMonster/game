package combat.abilities;

import farming.GardenPotTilePart;
import org.joml.Vector2f;
import tiles.TilePart;

public class GardenPotPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new GardenPotTilePart(position);
    }
}
