package tool.editor;

import buildings.RepairTask;
import buildings.RepairTaskNPC;
import engine.CoordinateMap;
import engine.core.Texture;
import entities.dungeons.ShooterEnemy;
import main.*;
import org.joml.Vector2f;
import tiles.*;
import tiles.doors.DungeonExitTilePart;
import tiles.doors.EntranceTilePart;
import tiles.npc.NpcDefinition;
import tiles.npc.ShopDefinitionRegistry;
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
                int xx = -5, yy = -2;
                int tx = x-xx, ty = y-yy;
                var type2 = TileType.get("base:scenes:restaurant:"+tx+":"+ty);
                if (type2 == null) return;
                var t = new DefaultTile(type2);
                t.setPart(new EntranceTilePart(new Vector2f(x, y), "HOME_WORLD", new Vector2f(4, -9), TextureIdMap.get("base:empty")));
                map.put(x, y, t);
            }
            case O2 -> {
                var t = new DefaultTile(TileType.get("ROAD"));
                t.setPart(new RepairTaskNPC(new Vector2f(x, y), ShopDefinitionRegistry.get("base:empty"), RepairTask.get("base:restaurant")));
                map.put(x, y, new DualTile(
                        new DefaultTile(TileType.get("ROAD")),
                        t, "base:restaurant_unlocked", context
                ));
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
