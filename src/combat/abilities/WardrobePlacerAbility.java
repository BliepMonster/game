package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.house.WardrobeTilePart;

public class WardrobePlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new WardrobeTilePart(position);
    }
}
