package main;

import engine.CoordinateMap;
import org.joml.Vector2f;
import tiles.Tile;

public class DungeonWorld extends World {
    public DungeonWorld(CoordinateMap<Tile> map, String address, String id) {
        super(map, address, id);
    }
    @Override
    public void onPlayerRespawn(GameContext context) {
        context.movePlayer(WorldRegistry.get("HOME_WORLD"), new Vector2f());
    }
}
