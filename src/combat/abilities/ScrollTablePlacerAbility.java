package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.ScrollCraftingTilePart;

public class ScrollTablePlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new ScrollCraftingTilePart(position);
    }
}
