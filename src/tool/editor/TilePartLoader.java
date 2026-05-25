package tool.editor;

import engine.json.JsonObject;
import engine.json.JsonString;
import main.GameContext;
import org.joml.Vector2i;
import tiles.CraftingStationTilePart;
import tiles.TilePart;
import tiles.resources.*;

public final class TilePartLoader {
    public static final String ID_IRON_ORE = "iron_ore";
    public static final String ID_COAL_ORE = "coal_ore";
    public static final String ID_COPPER_ORE = "copper_ore";
    public static final String ID_WHEAT = "wheat";
    public static final String ID_MAPLE_TREE = "tree_maple";
    public static final String ID_CARROTS = "carrots";
    public static final String ID_POTATOES = "potatoes";
    public static final String ID_STRAWBERRIES = "strawberries";
    public static final String ID_SPAWNER = "spawner";
    public static final String ID_CRAFTING_STATION = "crafting_station";

    public static TilePart load(Vector2i position, JsonString obj, GameContext context) {
        String type = obj.value;

        return switch (type) {
            case ID_IRON_ORE -> new IronOrePart();
            case ID_CARROTS -> new CarrotTilePart();
            case ID_SPAWNER -> SpawnerLoader.loadSpawner(position, context);
            case ID_STRAWBERRIES -> new StrawberryTilePart();
            case ID_POTATOES -> new PotatoTilePart();
            case ID_MAPLE_TREE -> new MapleTreePart();
            case ID_WHEAT -> new WheatTilePart();
            case ID_COPPER_ORE -> new CopperOrePart();
            case ID_COAL_ORE -> new CoalOrePart();
            case ID_CRAFTING_STATION -> new CraftingStationTilePart();
            default -> throw new RuntimeException("Unknown object: " + type);
        };
    }
}
