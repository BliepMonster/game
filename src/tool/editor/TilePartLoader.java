package tool.editor;

import engine.core.Texture;
import engine.core.text.Font;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import farming.CompostType;
import farming.CropType;
import farming.GardenPotTilePart;
import farming.PlantedCrop;
import main.GameContext;
import org.joml.Vector2f;
import org.joml.Vector2i;
import tiles.*;
import tiles.crafting.*;
import tiles.crafting.scrolls.ScrollApplicationTilePart;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.house.ChestTilePart;
import tiles.npc.ShopDefinitionRegistry;
import tiles.npc.DialogueNPC;
import tiles.resources.*;
import tiles.spawner.GemstoneGolemSpawner;
import tiles.spawner.GoldenZombieSpawner;
import tiles.spawner.ZombieSpawner;

import java.util.HashMap;

public final class TilePartLoader {
    public static final String ID_IRON_ORE = "iron_ore";
    public static final String ID_FURNACE = "furnace";
    public static final String ID_CRUSHER = "crusher";
    public static final String ID_SCROLL_TABLE = "scroll_crafter";
    public static final String ID_POLISHING_MACHINE = "polishing_machine";
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
    public static final String ID_RUBY = "ruby";
    public static final String ID_SCROLL_APPLIER = "scroll_applier";
    public static final String ID_SPAWNER_GEMGOLEM = "gg_spawner";
    public static final String ID_CHEST = "chest";
    public static final String ID_GLOWING_MUSHROOM = "glowing_mushroom";
    public static final String ID_PINE_TREE = "pine_tree";
    public static final String ID_GARDEN_POT = "garden_pot";
    public static Font font;
    public static final HashMap<String, Texture> tcache = new HashMap<>();

    public static EntranceTilePart loadEntrance(Vector2f tilePos, JsonObject object) {
        var tid = ((JsonString) object.values.get("texture_id")).value;
        if (!tcache.containsKey(tid)) tcache.put(tid, new Texture(tid));
        var wid = ((JsonString) object.values.get("world_id"));
        return new EntranceTilePart(
                tilePos,
                wid.value,
                new Vector2f(
                        ((JsonNumber) object.values.get("x")).num,
                        ((JsonNumber) object.values.get("y")).num
                ),
                tcache.get(tid)
        );
    }
    public static DungeonExitTilePart loadExit(Vector2f tilePos, JsonObject object) {
        var tid = ((JsonString) object.values.get("texture_id")).value;
        if (!tcache.containsKey(tid)) tcache.put(tid, new Texture(tid));
        var wid = ((JsonString) object.values.get("world_id"));
        return new DungeonExitTilePart(
                tilePos,
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
        var pos = new Vector2f(position.x, position.y);

        return switch (type) {
            case ID_IRON_ORE -> new IronOrePart(pos);
            case ID_CARROTS -> new CarrotTilePart(pos);
            case ID_STRAWBERRIES -> new StrawberryTilePart(pos);
            case ID_POTATOES -> new PotatoTilePart(pos);
            case ID_MAPLE_TREE -> new MapleTreePart(pos);
            case ID_WHEAT -> new WheatTilePart(pos);
            case ID_COPPER_ORE -> new CopperOrePart(pos);
            case ID_COAL_ORE -> new CoalOrePart(pos);
            case ID_CRAFTING_STATION -> new CraftingStationTilePart(pos);
            case ID_SHOP_NPC -> new DialogueNPC(pos, ShopDefinitionRegistry.get(((JsonString)obj.values.get("id")).value), font);
            case ID_TOMBSTONE -> new TombstoneTilePart(pos);
            case ID_ZOMBIE_SPAWNER -> new ZombieSpawner(pos, context);
            case ID_FURNACE -> new FurnaceTilePart(pos);
            case ID_ENTRANCE -> loadEntrance(pos, obj);
            case ID_DUNGEON_EXIT -> loadExit(pos, obj);
            case ID_GOLD_ORE -> new GoldOrePart(pos);
            case ID_GOLDEN_ZOMBIE_SPAWNER -> new GoldenZombieSpawner(pos, context);
            case ID_DECONSTRUCTOR -> new DeconstructorTilePart(pos);
            case ID_RUBY -> new RubyGemstoneTilePart(pos);
            case ID_SCROLL_APPLIER -> new ScrollApplicationTilePart(pos);
            case ID_SPAWNER_GEMGOLEM -> new GemstoneGolemSpawner(pos, context);
            case ID_CHEST -> ChestTilePart.load(pos, obj);
            case ID_GLOWING_MUSHROOM -> new GlowingMushroomTilePart(pos);
            case ID_CRUSHER -> new CrusherTilePart(pos);
            case ID_SCROLL_TABLE -> new ScrollCraftingTilePart(pos);
            case ID_PINE_TREE -> new PineTreePart(pos);
            case ID_POLISHING_MACHINE -> new PolishingMachineTilePart(pos);
            case ID_GARDEN_POT -> {
                var cropV = obj.values.get("crop");
                var nextCompostV = (JsonString) obj.values.get("nextCompost");
                var currentCompostV = (JsonString) obj.values.get("compost");
                yield new GardenPotTilePart(pos, cropV instanceof JsonString crop ?
                        new PlantedCrop(CropType.get(crop.value),
                                (long)((JsonNumber) obj.values.get("duration")).num,
                                currentCompostV != null ?
                                        CompostType.get((currentCompostV).value) :
                                        null) :
                        null,
                        nextCompostV instanceof JsonString compost ?
                                CompostType.get(compost.value) :
                                null
                );
            }
            default -> throw new RuntimeException("Unknown object: " + type);
        };
    }
}
