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
import tiles.npc.DialogueNPC;
import tiles.npc.NpcDefinition;
import tiles.npc.ShopDefinitionRegistry;
import tiles.resources.*;
import tiles.resources.ores.SapphireGemstoneTilePart;
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
                tile.setPart(new EntranceTilePart(new Vector2f(x, y), "GEMSTONE_CAVE", new Vector2f(-29, 3), TextureIdMap.get("base:empty")));
            }
            case O3 -> {
                map.put(x, y, new WallTile(TileType.get("ROAD")));
            }
            case O4 -> tile.setPart(new SapphireGemstoneTilePart(new Vector2f(x, y)));
            case O5 -> ((TextureAdaptTilePart) tile.getPart()).setPart(new DialogueNPC(new Vector2f(x, y), ShopDefinitionRegistry.get("chef"), Main.font));
            case O6 -> tile.setPart(new UnstandableTilePart(new Vector2f(x, y)));
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
