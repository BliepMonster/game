package tiles.crafting.scrolls;

import crafting.FullItemStorage;
import engine.gui.GUI;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import tiles.TilePart;
import tool.editor.TilePartLoader;

import java.awt.*;

public class ScrollApplicationTilePart extends TilePart {
    public ScrollApplicationTilePart(Vector2f position) {
        super(position);
    }

    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.cyan, x, y, 1, 1),
                -y
        );
    }

    @Override
    public boolean isStandable() {
        return false;
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {
        var player = context.getPlayer();
        var inv = new FullItemStorage(player.items, player.inventory, player.currency);
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new ScrollApplicationWidget(inv, player), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_SCROLL_APPLIER);
    }
}
