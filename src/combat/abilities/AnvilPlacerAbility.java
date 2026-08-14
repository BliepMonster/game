package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.AnvilTilePart;

public class AnvilPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new AnvilTilePart(position);
    }
}
