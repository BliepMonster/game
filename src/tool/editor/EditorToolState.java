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
import tiles.resources.ores.GoldOrePart;
import tiles.resources.ores.SilverOrePart;


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
                int xx = -5, yy = -1;
                TileType built = TileType.get("base:scenes:seed_store:" + (x - xx) + ":" + (y - yy));
                if (built == null ) return;
                Tile a = new DefaultTile(built);
                map.put(x, y, a);
            }
            case O3 -> {
                tile.setPart(new SilverOrePart(new Vector2f(x, y)));
            }
            case O4 -> tile.setPart(new EntranceTilePart(new Vector2f(x, y), "GOLD_CAVE_WORLD", new Vector2f(-30, 16), TextureIdMap.get("base:placeholder")));
            case O5 -> map.put(x, y, new DefaultTile(TileType.get("ROAD")));
            case O6 -> tile.setPart(new GoldOrePart(new Vector2f(x, y)));
            case O7 -> map.put(x, y, new DefaultTile(TileType.get("WALL")));
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
