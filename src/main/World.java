package main;

import engine.CoordinateMap;
import engine.hitboxes.CompoundHitbox;
import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import engine.hitboxes.Unpositioned;
import entities.Entity;
import entities.Player;
import tiles.DefaultTile;
import tiles.Tile;
import tiles.TileType;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final CoordinateMap<Tile> map = CoordinateMap.newDefaultPackedMap();
    private final List<Entity> entities = new ArrayList<>();
    public World() {
        for (int i = -5; i < 5; ++i) {
            for (int j = -5; j < 5; ++j) {
                if (i == 4 || j == 4 || i == -5 || j == -5)
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_WALL]));
                else
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_GRASS]));
            }
        }
    }
    public Hitbox getHitbox() {
        var hitbox = new CompoundHitbox();
        for (var tile : map) {
            if (!tile.element().isStandable()) {
                hitbox.add(new RectangularHitbox(tile.x(), tile.y(), 1, 1, Unpositioned.INSTANCE));
            }
        }
        return hitbox;
    }
    public void tick() {
        var hitbox = getHitbox();
        entities.forEach(Entity::tick);
        entities.forEach(entity -> entity.move(hitbox));
    }
    public void render(RenderContext context, Player player, float tsize) {
        var bounds = map.getBounds();
        var minX = bounds.x;
        var xSize = bounds.y;
        var minY = bounds.z;
        var ySize = bounds.w;
        var y = player.getY();
        var x = player.getX();
        for (int i = minX; i < minX + xSize; ++i) {
            var xoff = i-x;
            for (int j = minY; j < minY+ySize; ++j) {
                var option = map.get(i, j);
                if (option.isPresent()) {
                    var tile = option.get();
                    tile.render(context.map(), tsize*xoff, tsize*(j-y), tsize, tsize);
                }
            }
        }
    }
}
