package main;

import engine.CoordinateMap;
import engine.core.Screen;
import engine.core.input.Input;
import engine.hitboxes.*;
import entities.Entity;
import entities.Player;
import tiles.DefaultTile;
import tiles.Tile;
import tiles.TileType;
import tiles.resources.StrawberryTilePart;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final CoordinateMap<Tile> map;
    public final List<Entity> entities = new ArrayList<>();
    public World(CoordinateMap<Tile> map) {
        this.map = map;
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
    public void tick(Game game, boolean pressed, Screen screen, float tsize) {
        var hitbox = getHitbox();
        entities.forEach(entity -> entity.tick(game));
        entities.forEach(entity -> entity.move(hitbox));
        if (pressed) {
            float x = Input.getMouseX(screen);
            float y = Input.getMouseY(screen);
            float px = x/tsize+game.player.getX();
            float py = y/tsize+game.player.getY();
            map.get((int) Math.floor(px), (int) Math.floor(py)).ifPresent(tile -> tile.onClick(game));
        }
    }
    public void render(RenderContext context, Player player, float tsize) {
        var bounds = map.getBounds();
        var minX = bounds.x;
        var xSize = bounds.y;
        var minY = bounds.z;
        var ySize = bounds.w;
        var y = player.getY();
        var x = player.getX();
        for (int i = minX; i <= minX + xSize; ++i) {
            var xoff = i-x;
            for (int j = minY; j <= minY+ySize; ++j) {
                var option = map.get(i, j);
                if (option.isPresent()) {
                    var tile = option.get();
                    tile.render(context.map(), tsize*xoff, tsize*(j-y), tsize, tsize);
                }
            }
        }
    }
    private static void addStrawberriesToDefaultWorld(CoordinateMap<Tile> map, Player player) {
        map.get(15, 15).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(15, 16).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(17, 14).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(18, 15).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(18, 18).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(12, 16).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(12, 13).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(14, 14).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(13, 18).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        map.get(17, 12).ifPresent(tile -> tile.setPart(new StrawberryTilePart(player)));
        // TODO: a proper system + expand this area
    }
    public static World home(Player player) {
        CoordinateMap<Tile> map = CoordinateMap.newDefaultPackedMap();
        for (int i = -20; i < 20; ++i) {
            for (int j = -20; j < 20; ++j) {
                if (i == -20 || j == -20 || i == 19 || j == 19) {
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_WALL]));
                } else {
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_GRASS]));
                }
            }
        }
        addStrawberriesToDefaultWorld(map, player);
        return new World(map);
    }
}
