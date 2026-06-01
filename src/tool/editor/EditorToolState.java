package tool.editor;

import engine.CoordinateMap;
import engine.core.Texture;
import entities.DumbZombieEnemy;
import entities.Entity;
import entities.dungeons.SpeedsterEnemy;
import main.EntityProducer;
import main.Game;
import main.GameContext;
import main.World;
import org.joml.Vector2f;
import tiles.*;
import tiles.crafting.DeconstructorTilePart;
import tiles.crafting.FurnaceTilePart;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.resources.*;
import tiles.spawner.GoldenZombieSpawner;

import java.lang.reflect.Constructor;

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
            case O1 -> map.put(x, y, new DefaultTile(TileType.WALL));
            case O2 -> tile.setPart(new EntranceTilePart(
                    "DUNGEON1_WORLD",
                    new Vector2f(),
                    new Texture("res/bred.png")
            ));
            case O3 -> tile.setPart(new CarrotTilePart());
            case O4 -> map.put(x, y, new DefaultTile(TileType.ROAD));
            case O5 -> tile.setPart(new TombstoneTilePart());
            case O6 -> tile.setPart(null);
            case O7 -> map.put(x, y, new DefaultTile(TileType.DIRT_PATH));
            case O8 -> {
                if (Game.getTickCount()-lastTick < 20) return;
                try {
                    world.onLoadEntities.add(
                            new EntityProducer(
                                    SpeedsterEnemy.class.getConstructor(Vector2f.class),
                                    x,
                                    y
                            )
                    );
                } catch (Exception _) {
                    throw new RuntimeException();
                }
                lastTick = Game.getTickCount();
            }
            case O9 -> tile.setPart(new DungeonExitTilePart("HOME_WORLD", new Vector2f(), new Texture("res/bred.png")));
        }
    }
}
