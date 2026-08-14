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
                System.out.println(new Vector2f(x, y));
            }
            case O2 -> {
                map.put(x, y, new DefaultTile(TileType.get("WATER")));
            }
            case O3 -> {
                map.put(x, y, new DefaultTile(TileType.get("GRASS_TAIGA")));
            }
            case O4 -> map.put(x, y, new DefaultTile(TileType.get("ROAD")));
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
