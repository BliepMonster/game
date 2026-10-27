package tool.editor;

import buildings.RepairTask;
import buildings.RepairTaskNPC;
import engine.CoordinateMap;
import engine.core.textures.Texture;
import entities.dungeons.SapphireGolemDungeonEnemy;
import main.*;
import org.joml.Vector2f;
import tiles.*;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.doors.TrapdoorTilePart;
import tiles.npc.ShopDefinitionRegistry;


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
    public static int lastTick = 0;
    public void apply(Tile tile, CoordinateMap<Tile> map, int x, int y, World world, GameContext context) {
        switch (this) {
            case O1 -> {
                System.out.println(new Vector2f(x, y));
            }
            case O2 -> {
                int xx = -36, yy = -11;
                TileType built = TileType.get("base:tiles:bamboo_house:" + (x - xx) + ":" + (y - yy));
                if (built == null) return;
                var v = new RenderOverTile(new DefaultTile(TileType.get("GRASS")), built);
                Tile a = new DualTile(v, new RenderOverTile(new DefaultTile(TileType.get("COMPOST")), TileType.get("base:tiles:bamboo_house_empty:" + (x - xx) + ":" + (y - yy)), 8000), "base:bamboo_house_unlocked", context);

                map.put(x, y, a);
            }
            case O3 -> {
                int xx = 0, yy = 0;
                TileType built = TileType.get("base:scenes:bamboo_house:" + (x - xx) + ":" + (y - yy));
                if (built == null) return;
                map.put(x, y, new DefaultTile(built));
            }
            case O4 ->
                    tile.setPart(null);
            case O5 ->
                    ((DualTile) tile).a.setPart(new EntranceTilePart(new Vector2f(x, y), "base:bamboo_house", new Vector2f(2, 2), TextureIdMap.get("base:empty")));
            case O6 ->
                    map.put(x, y, new DefaultTile(TileType.get("base:tiles:path_grass:ml")));
            case O7 -> context.movePlayer(WorldRegistry.get("base:bamboo_house"), new Vector2f(x, y));
            case O8 -> {
                map.put(x, y, new DefaultTile(TileType.get("DIRT_PATH")));
            }
            case O9 -> {
                System.out.println(tile.saveWithPart());
            }
        }
    }
}
