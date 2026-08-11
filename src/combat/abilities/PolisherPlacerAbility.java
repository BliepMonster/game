package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.PolishingMachineTilePart;

public class PolisherPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new PolishingMachineTilePart(position);
    }
}
