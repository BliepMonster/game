package tool.editor;

import engine.CoordinateMap;
import engine.core.Texture;
import entities.MinecartSpawnerEntity;
import entities.dungeons.MinerDungeonEnemy;
import entities.dungeons.SapphireGolemDungeonEnemy;
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
                int xx = 0, yy = 0;
                TileType built = TileType.get("base:scenes:sapphire_f3:" + (x - xx) + ":" + (y - yy));
                if (built == null) return;
                Tile a = new DefaultTile(built);
                map.put(x, y, a);
            }
            case O3 -> {
                tile.setPart(new UnstandableTilePart(new Vector2f(x, y)));
            }
            case O4 -> tile.setPart(new DungeonExitTilePart(new Vector2f(x, y), "SAPPHIRE_CAVE", new Vector2f(4, 28), TextureIdMap.get("base:empty")));
            case O5 -> tile.setPart(new DungeonExitTilePart(new Vector2f(x, y), "SAPPHIRE_DUNGEON_3", new Vector2f(9.5f, 1), TextureIdMap.get("base:empty")));
            case O6 -> tile.setPart(null);
            case O7 -> map.put(x, y, new DefaultTile(TileType.get("WALL")));
            case O8 -> {
                if (Game.getTickCount()-lastTick < 20) return;
                try {
                    world.onLoadEntities.add(
                            new EntityProducer(
                                    SapphireGolemDungeonEnemy.class.getConstructor(Vector2f.class),
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
