package tiles.crafting;

import crafting.ui.DeconstructorPanel;
import engine.core.Texture;
import engine.gui.GUI;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;
import tiles.TilePart;
import tool.editor.TilePartLoader;

public class DeconstructorTilePart extends TilePart {
    public DeconstructorTilePart(Vector2f position) {
        super(position);
    }
    @Override
    public boolean isStandable() {
        return false;
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_DECONSTRUCTOR);
    }

    @Override
    public void onClick(GameContext context) {
        if (GUI.getAll(0).isEmpty()) {
            GUI.toggle(62);
            GUI.add(new DeconstructorPanel(
                    context.getPlayer()
            ), 0);
        }
    }

    @Override
    public void onStand(GameContext context) {}

    public static final Texture TEXTURE = new Texture("res/textures/tiles/deconstructor.png");
    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(TEXTURE, x, y, 1, 1),
                -y
        );
    }
    @Override
    public ItemDefinition getItemDefinition() {
        return ItemDefinition.get("base:placers:deconstructor");
    }
}
