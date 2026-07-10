package tiles.resources;

import main.GameContext;
import org.joml.Vector2f;

public abstract class CropTilePart extends ResourceCreator {
    public CropTilePart(ResourceDefinition definition, Vector2f position) {
        super(definition, position);
    }
    public boolean isReady() {
        return ready;
    }
    public void harvestWithPenalty(GameContext context, float penalty) {
        penalty(context, penalty);
    }
}
