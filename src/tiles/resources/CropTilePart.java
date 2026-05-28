package tiles.resources;


import main.GameContext;

public abstract class CropTilePart extends ResourceCreator {
    public CropTilePart(ResourceDefinition definition) {
        super(definition);
    }
    public boolean isReady() {
        return ready;
    }
    public void harvestWithPenalty(GameContext context, float penalty) {
        penalty(context, penalty);
    }
}
