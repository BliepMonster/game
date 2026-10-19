package tool.editor;

import engine.CoordinateMap;
import engine.core.textures.Texture;
import entities.dungeons.SapphireGolemDungeonEnemy;
import main.*;
import org.joml.Vector2f;
import tiles.*;
import tiles.doors.DungeonExitTilePart;


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
                int xx = 0, yy = 0;
                TileType built = TileType.get("base:scenes:sapphire_f3:" + (x - xx) + ":" + (y - yy));
                if (built == null) return;
                Tile a = new DefaultTile(built);
                map.put(x, y, a);
            }
            case O3 -> {
                map.put(x, y, new DefaultTile(TileType.get("base:tiles:taiga_path_grass:mm")));
            }
            case O4 ->
                    map.put(x, y, new DefaultTile(TileType.get("base:tiles:taiga_path_grass:tr2")));
            case O5 ->
                    map.put(x, y, new DefaultTile(TileType.get("base:tiles:taiga_path_grass:tl2")));
            case O6 ->
                    map.put(x, y, new DefaultTile(TileType.get("base:tiles:taiga_path_grass:bl2")));
            case O7 -> map.put(x, y, new DefaultTile(TileType.get("base:tiles:taiga_path_grass:br2")));
            case O8 -> {
                map.put(x, y, new DefaultTile(TileType.get("base:tiles:path_grass:br2")));
            }
            case O9 -> {
                var t = new DefaultTile(TileType.get("WALL"));
                t.setPart(tile == null ? null : tile.getPart());
                map.put(x, y, t);
            }
        }
    }
}
