package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import engine.core.textures.Texture;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import tiles.Tile;

import java.util.HashMap;

public abstract class ItemDefinition {
    public final String id, name;
    public final String description;
    public final Texture texture;
    public int lastUse = 0;
    public static final HashMap<String, ItemDefinition> definitions = new HashMap<>();
    public ItemDefinition(String id, String name, Texture texture) {
        this.id = id;
        this.name = name;
        this.description = "";
        this.texture = texture;
        register(this);
    }
    public ItemDefinition(String id, String name, String description, Texture texture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.texture = texture;
        register(this);
    }
    public static ItemDefinition get(String id) {
        if (!definitions.containsKey(id)) throw new RuntimeException("Invalid ID: "+id);
        return definitions.get(id);
    }
    public StatHolder getOnConsumeStats() {
        return new StatHolder();
    }
    public static void register(ItemDefinition definition) {
        definitions.put(definition.id, definition);
    }
    public abstract ItemUseResult use(Tile tile, Item item, GameContext context, Vector2f worldPos);
    public int getCooldownTime() {
        return 0;
    }
    public StatHolder getHeldStats_(Item item, GameContext context) {
        return getHeldStats(item, context).merge(item.scrolls.stats());
    }
    public StatHolder getWornStats_(Item item, GameContext context) {
        if (getEquipmentSlot() == EquipmentSlot.NONE) return new StatHolder();
        return getWornStats(item, context).merge(item.scrolls.stats());
    }
    public abstract StatHolder getHeldStats(Item item, GameContext context);
    public abstract StatHolder getWornStats(Item item, GameContext context);
    public abstract EquipmentSlot getEquipmentSlot();
    public abstract boolean canApplyScrolls();
}
