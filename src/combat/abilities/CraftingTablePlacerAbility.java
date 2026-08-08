package combat.abilities;

import org.joml.Vector2f;
import tiles.TilePart;
import tiles.crafting.CraftingStationTilePart;

public class CraftingTablePlacerAbility extends PlacerAbility {
    @Override
    public TilePart getPart(Vector2f position) {
        return new CraftingStationTilePart(position);
    }
}
