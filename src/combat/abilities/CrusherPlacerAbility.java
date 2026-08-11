package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.CrusherTilePart;

public class CrusherPlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new CrusherTilePart(position);
    }
}
