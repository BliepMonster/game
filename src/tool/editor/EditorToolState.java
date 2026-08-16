package tool.editor;

import engine.CoordinateMap;
import engine.core.Texture;
import entities.dungeons.ShooterEnemy;
import main.*;
import org.joml.Vector2f;
import tiles.*;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.resources.*;
import tiles.spawner.GemstoneGolemSpawner;


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
                int xx = 2, yy = -9;
                int tx = x-xx, ty = y-yy;
                var type = TileType.get("base:tiles:restaurant_empty"+tx+ty);
                if (type == null) return;
                var type2 = TileType.get("base:tiles:restaurant"+tx+ty);
                var road = TileType.get("ROAD");
                var compost = TileType.get("COMPOST");
                if (ty > 5) map.put(x, y, new DualTile(new RenderOverTile(new DefaultTile(road), type2, 8000), new RenderOverTile(new DefaultTile(road), type), "base:restaurant_unlocked", context));
                else map.put(x, y, new DualTile(new RenderOverTile(new DefaultTile(road), type2), new RenderOverTile(new DefaultTile(compost), type), "base:restaurant_unlocked", context));
            }
            case O2 -> {
                map.put(x, y, new WallTile(TileType.get("ROAD")));
            }
            case O3 -> {
                map.put(x, y, new WallTile(TileType.get("base:tiles:wall_bottom")));
            }
            case O4 -> map.put(x, y, new WallTile(TileType.get("base:tiles:wall_right:3")));
            case O5 -> tile.setPart(new LeftWheatTilePart(new Vector2f(x, y)));
            case O6 -> tile.setPart(null);
            case O7 -> map.put(x, y, new DefaultTile(TileType.get("GRASS")));
            case O8 -> {
                if (Game.getTickCount()-lastTick < 20) return;
                try {
                    world.onLoadEntities.add(
                            new EntityProducer(
                                    ShooterEnemy.class.getConstructor(Vector2f.class),
                                    x,
                                    y
                            )
                    );
                } catch (Exception _) {
                    throw new RuntimeException();
                }
                lastTick = Game.getTickCount();
            }
            case O9 -> tile.setPart(new DungeonExitTilePart(new Vector2f(x, y), "HOME_WORLD", new Vector2f(), new Texture("res/bred.png")));
        }
    }
}
