package tiles.house;

import engine.core.textures.Texture;
import engine.gui.GUI;
import engine.json.JsonArray;
import engine.json.JsonNull;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import main.TextureIdMap;
import org.joml.Vector2f;
import saving.SavedItem;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import tiles.TilePart;
import tool.editor.TilePartLoader;

public class ChestTilePart extends TilePart {
    public static final int NUM_ITEMS_PER_ROW = 10;
    public static final int NUM_ROWS = 3;
    public static final int NUM_ITEMS = NUM_ITEMS_PER_ROW * NUM_ROWS;
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:chest");
    private final Item[] items = new Item[NUM_ITEMS];

    public ChestTilePart(Vector2f position) {
        super(position);
    }

    public Item get(int index) {
        return items[index];
    }
    public void set(int index, Item item) {
        items[index] = item;
    }
    public boolean add(Item item) {
        for (int i = 0; i < NUM_ITEMS; ++i) {
            if (items[i] == null) {
                items[i] = item;
                return true;
            }
        }
        return false;
    }

    public boolean canBreak() {
        for (Item item : items) if (item != null) return false;
        return true;
    }
    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(TEXTURE, x, y, 1, 1.5f),
                -y
        );
    }

    @Override
    public boolean isStandable() {
        return false;
    }

    @Override
    public void onStand(GameContext context) {}

    @Override
    public void onClick(GameContext context) {
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new ChestWidget(context.getPlayer().items, this), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        var map = new JsonObject();
        map.values.put("type", new JsonString(TilePartLoader.ID_CHEST));
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

    public static ChestTilePart load(Vector2f position, JsonObject object) {
        var chest = new ChestTilePart(position);
        if (!(object.values.get("items") instanceof JsonArray array)) return chest;
        for (int i = 0; i < array.values.size() && i < NUM_ITEMS; ++i) {
            var value = array.values.get(i);
            if (value instanceof JsonNull) continue;
            var item = (JsonObject) value;
            var s = item.toSavedObject();
            var si = SavedItem.from(s);
            chest.items[i] = new Item(si);
        }
        return chest;
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return ItemDefinition.get("base:placers:chest");
    }
}
