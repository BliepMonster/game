package tiles.npc;

import crafting.FullItemStorage;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import tiles.TilePart;
import tiles.npc.ui.ShopPanel;
import tiles.npc.ui.ShopWrapper;
import tool.editor.TilePartLoader;

public class ShopNPC extends TilePart {
    private final ShopNpcDefinition definition;
    private final Font font;
    public ShopNPC(Vector2f position, ShopNpcDefinition definition, Font font) {
        super(position);
        this.definition = definition;
        this.font = font;
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
        if (GUI.getAll(0).isEmpty()) {
            var player = context.getPlayer();
            GUI.add(new ShopWrapper(definition, font, new FullItemStorage(player.items, player.inventory, player.currency), context), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString(TilePartLoader.ID_SHOP_NPC));
        object.values.put("id", new JsonString(definition.id()));
        return object;
    }
}
