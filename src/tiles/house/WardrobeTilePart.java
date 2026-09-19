package tiles.house;

import engine.core.Texture;
import engine.json.JsonArray;
import engine.json.JsonNull;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import engine.gui.GUI;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import saving.SavedItem;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import tiles.TilePart;
import tool.editor.TilePartLoader;

public class WardrobeTilePart extends TilePart {
    public static final Texture TEX = new Texture("res/textures/tiles/wardrobe.png");
    final Item[] items = new Item[5];
    public WardrobeTilePart(Vector2f pos, Item... items) {
        super(pos);
        System.arraycopy(items, 0, this.items, 0, items.length);
    }

    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(() -> camera.image(TEX, x, y, 1, 3), -y);
    }

    @Override
    public boolean isStandable() {
        return false;
    }

    @Override
    public void onStand(GameContext context) {

    }

    @Override
    public boolean canBreak() {
        for (Item item : items) {
            if (item != null) return false;
        }
        return true;
    }

    @Override
    public void onClick(GameContext context) {
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new WardrobePanel(this, context.getPlayer()), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        var map = new JsonObject();
        map.values.put("type", new JsonString(TilePartLoader.ID_WARDROBE));
        var array = new JsonArray();
        for (var item : items) {
            if (item == null) {
                array.values.add(JsonNull.INSTANCE);
            } else {
                var s = item.save();
                var object = s.save().toJSON();
                array.values.add(object);
            }
        }
        map.values.put("items", array);
        return map;
    }

    public static WardrobeTilePart load(Vector2f position, JsonObject object) {
        var wardrobe = new WardrobeTilePart(position);
        if (object.values.get("items") instanceof JsonArray array) {
            for (int i = 0; i < array.values.size() && i < 5; ++i) {
                var value = array.values.get(i);
                if (value instanceof JsonNull) continue;
                var item = (JsonObject) value;
                var s = item.toSavedObject();
                var si = SavedItem.from(s);
                wardrobe.items[i] = new Item(si);
            }
        }
        return wardrobe;
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return ItemDefinition.get("base:placers:wardrobe");
    }
}
