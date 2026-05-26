package tool.editor;

import engine.CoordinateMap;
import tiles.CraftingStationTilePart;
import tiles.DefaultTile;
import tiles.Tile;
import tiles.TileType;
import tiles.npc.ShopDefinitionRegistry;
import tiles.npc.ShopNPC;
import tiles.resources.*;

public enum EditorToolState {
    PLACE_TREE,
    PLACE_IRON,
    PLACE_STRAWBERRIES,
    PLACE_COAL,
    PLACE_COPPER,
    ERASE,
    MAKE_PATH,
    MAKE_GRASS,
    PLACE_NPC,
    ;
    public void apply(Tile tile, CoordinateMap<Tile> map, int x, int y) {
        switch (this) {
            case PLACE_TREE -> tile.setPart(new MapleTreePart());
            case PLACE_IRON -> tile.setPart(new IronOrePart());
            case PLACE_STRAWBERRIES -> tile.setPart(new StrawberryTilePart());
            case PLACE_COAL -> tile.setPart(new CoalOrePart());
            case PLACE_COPPER -> tile.setPart(new CopperOrePart());
            case ERASE -> tile.setPart(null);
            case MAKE_PATH -> map.put(x, y, new DefaultTile(TileType.DIRT_PATH));
            case MAKE_GRASS -> map.put(x, y, new DefaultTile(TileType.GRASS));
            case PLACE_NPC -> tile.setPart(new ShopNPC(ShopDefinitionRegistry.get("void"), TilePartLoader.font));
        }
    }
}
