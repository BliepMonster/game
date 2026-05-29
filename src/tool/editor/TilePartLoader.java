package tool.editor;

import engine.core.Texture;
import engine.core.text.Font;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import main.GameContext;
import org.joml.Vector2f;
import org.joml.Vector2i;
import tiles.*;
import tiles.crafting.CraftingStationTilePart;
import tiles.crafting.DeconstructorTilePart;
import tiles.crafting.FurnaceTilePart;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.npc.ShopDefinitionRegistry;
import tiles.npc.ShopNPC;
import tiles.resources.*;
import tiles.spawner.GoldenZombieSpawner;
import tiles.spawner.ZombieSpawner;

import java.util.HashMap;

public final class TilePartLoader {
    public static final String ID_IRON_ORE = "iron_ore";
    public static final String ID_FURNACE = "furnace";
    public static final String ID_COAL_ORE = "coal_ore";
    public static final String ID_COPPER_ORE = "copper_ore";
    public static final String ID_WHEAT = "wheat";
    public static final String ID_MAPLE_TREE = "tree_maple";
    public static final String ID_CARROTS = "carrots";
    public static final String ID_POTATOES = "potatoes";
    public static final String ID_STRAWBERRIES = "strawberries";
    public static final String ID_CRAFTING_STATION = "crafting_station";
    public static final String ID_SHOP_NPC = "shop_npc";
    public static final String ID_TOMBSTONE = "tombstone";
    public static final String ID_ZOMBIE_SPAWNER = "zombie_spawner";
    public static final String ID_ENTRANCE = "entrance";
    public static final String ID_GOLDEN_ZOMBIE_SPAWNER = "golden_zombie_spawner";
    public static final String ID_GOLD_ORE = "gold_ore";
    public static final String ID_DECONSTRUCTOR = "deconstructor";
    public static final String ID_DUNGEON_EXIT = "dungeon_exit";
    public static Font font;
    public static final HashMap<String, Texture> tcache = new HashMap<>();

    public static EntranceTilePart loadEntrance(JsonObject object) {
        var tid = ((JsonString) object.values.get("texture_id")).value;
        if (!tcache.containsKey(tid)) tcache.put(tid, new Texture(tid));
        var wid = ((JsonString) object.values.get("world_id"));
        return new EntranceTilePart(
                wid.value,
                new Vector2f(
                        ((JsonNumber) object.values.get("x")).num,
                        ((JsonNumber) object.values.get("y")).num
                ),
                tcache.get(tid)
        );
    }
    public static DungeonExitTilePart loadExit(JsonObject object) {
        var tid = ((JsonString) object.values.get("texture_id")).value;
        if (!tcache.containsKey(tid)) tcache.put(tid, new Texture(tid));
        var wid = ((JsonString) object.values.get("world_id"));
        return new DungeonExitTilePart(
                wid.value,
                new Vector2f(
                        ((JsonNumber) object.values.get("x")).num,
                        ((JsonNumber) object.values.get("y")).num
                ),
                tcache.get(tid)
        );
    }

    public static TilePart load(Vector2i position, JsonObject obj, GameContext context) {
        String type = ((JsonString) obj.values.get("type")).value;

        return switch (type) {
            case ID_IRON_ORE -> new IronOrePart();
            case ID_CARROTS -> new CarrotTilePart();
            case ID_STRAWBERRIES -> new StrawberryTilePart();
            case ID_POTATOES -> new PotatoTilePart();
            case ID_MAPLE_TREE -> new MapleTreePart();
            case ID_WHEAT -> new WheatTilePart();
            case ID_COPPER_ORE -> new CopperOrePart();
            case ID_COAL_ORE -> new CoalOrePart();
            case ID_CRAFTING_STATION -> new CraftingStationTilePart();
            case ID_SHOP_NPC -> new ShopNPC(ShopDefinitionRegistry.get(((JsonString)obj.values.get("id")).value), font);
            case ID_TOMBSTONE -> new TombstoneTilePart();
            case ID_ZOMBIE_SPAWNER -> new ZombieSpawner(new Vector2f(position.x, position.y), context);
            case ID_FURNACE -> new FurnaceTilePart();
            case ID_ENTRANCE -> loadEntrance(obj);
            case ID_DUNGEON_EXIT -> loadExit(obj);
            case ID_GOLD_ORE -> new GoldOrePart();
            case ID_GOLDEN_ZOMBIE_SPAWNER -> new GoldenZombieSpawner(new Vector2f(position.x, position.y), context);
            case ID_DECONSTRUCTOR -> new DeconstructorTilePart();
            default -> throw new RuntimeException("Unknown object: " + type);
        };
    }
}
