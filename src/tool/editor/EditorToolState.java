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
    O1,
    O2,
    O3,
    O4,
    O5,
    O6,
    O7,
    O8,
    O9,
    ;
    public void apply(Tile tile, CoordinateMap<Tile> map, int x, int y) {
        switch (this) {
            case O1 -> map.put(x, y, new DefaultTile(TileType.ROAD));
            case O2 -> tile.setPart(new PotatoTilePart());
            case O3 -> tile.setPart(new CarrotTilePart());
            case O4 -> map.put(x, y, new DefaultTile(TileType.GRASS_DARK));
            case O5 -> tile.setPart(new CopperOrePart());
            case O6 -> tile.setPart(null);
            case O7 -> map.put(x, y, new DefaultTile(TileType.DIRT_PATH));
            case O8 -> map.put(x, y, new DefaultTile(TileType.GRASS));
            case O9 -> tile.setPart(new WheatTilePart());
        }
    }
}
