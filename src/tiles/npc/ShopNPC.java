package tiles.npc;

import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import tiles.TilePart;
import tool.editor.TilePartLoader;

public class ShopNPC extends TilePart {
    private final ShopNpcDefinition definition;
    public ShopNPC(ShopNpcDefinition definition) {
        this.definition = definition;
    }
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(definition.texture(), x, y, 1, definition.height()),
                -y
        );
    }
    public boolean isStandable() {
        return true;
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {
        // TODO
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString(TilePartLoader.ID_SHOP_NPC));
        object.values.put("id", new JsonString(definition.id()));
        return object;
    }
}
