package main;

import engine.CoordinateMap;
import engine.core.Screen;
import engine.core.input.Input;
import engine.hitboxes.*;
import entities.Entity;
import entities.Player;
import entities.TestEnemy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import tiles.DefaultTile;
import tiles.MagmaTile;
import tiles.Tile;
import tiles.TileType;
import tiles.resources.StrawberryTilePart;
import tiles.resources.WheatTilePart;

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
    public void tick(Game game, boolean pressed, Screen screen, float tsize, boolean canClick) {
        var hitbox = getHitbox();
        entities.forEach(entity -> entity.tick(game));
        entities.forEach(entity -> entity.move(hitbox));
        for (var entity : entities) {
            if (entity.shouldDie(game)) entity.die(game);
        }
        entities.removeIf(entity -> entity.shouldDie(game));
        var player = game.player;
        int left   = (int)(player.getX());
        int right  = (int)((player.getX() + 1));

        int top    = (int)(player.getY());
        int bottom = (int)((player.getY() + .2f));
        for (int tx = left; tx <= right; tx++) {
            for (int ty = top; ty <= bottom; ty++) {
                map.get(tx, ty).ifPresent(tile -> tile.onStand(game));
            }
        }
        if (pressed && canClick) {
            float x = Input.getMouseX(screen);
            float y = Input.getMouseY(screen);
            float px = x/tsize+player.getX();
            float py = y/tsize+player.getY();
            var v = map.get((int) Math.floor(px), (int) Math.floor(py));
            var t = v.orElse(null);
            var used = game.player.useSelectedItem(game, t, new Vector2f(px, py));
            if (v.isPresent()) {
                if (!used) {
                    t.onClick(game);
                }
            }
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
        entities.forEach(entity -> entity.render(context, (entity.getX()-x)*tsize, (entity.getY()-y)*tsize, tsize));
    }
    public void spawn(Entity entity) {
        this.entities.add(entity);
    }
    private static void addStrawberriesToDefaultWorld(CoordinateMap<Tile> map) {
        map.get(15, 15).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(15, 16).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(17, 14).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(18, 15).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(18, 18).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(12, 16).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(12, 13).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(14, 14).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(13, 18).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        map.get(17, 12).ifPresent(tile -> tile.setPart(new StrawberryTilePart()));
        // TODO: a proper system + expand this area
    }
    private static void addWheatToDefaultWorld(CoordinateMap<Tile> map) {
        for (int i = 18; i > 14; --i) {
            for (int j = -19; j < -12; ++j) {
                map.get(i, j).ifPresent(tile -> tile.setPart(new WheatTilePart()));
            }
        }
    }
    @Contract(" -> new")
    public static @NotNull World home() {
        CoordinateMap<Tile> map = CoordinateMap.newDefaultPackedMap();
        for (int i = -20; i < 20; ++i) {
            for (int j = -20; j < 20; ++j) {
                if (i == -20 || j == -20 || i == 19 || j == 19) {
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_WALL]));
                } else if (i < -10) {
                    map.put(i, j, new MagmaTile());
                } else {
                    map.put(i, j, new DefaultTile(TileType.types[TileType.TILETYPE_GRASS]));
                }
            }
        }
        addStrawberriesToDefaultWorld(map);
        addWheatToDefaultWorld(map);
        World world = new World(map);
        world.spawn(new TestEnemy(new Vector2f(-5, -5)));
        world.spawn(new TestEnemy(new Vector2f(-6, -5)));
        world.spawn(new TestEnemy(new Vector2f(-8, -5)));
        world.spawn(new TestEnemy(new Vector2f(-5, -3)));
        world.spawn(new TestEnemy(new Vector2f(6, -3)));
        world.spawn(new TestEnemy(new Vector2f(8, -3)));
        world.spawn(new TestEnemy(new Vector2f(5, -4)));
        world.spawn(new TestEnemy(new Vector2f(7, -5)));
        world.spawn(new TestEnemy(new Vector2f(-5, 5)));
        world.spawn(new TestEnemy(new Vector2f(-5, 6)));
        world.spawn(new TestEnemy(new Vector2f(-6, 6)));
        world.spawn(new TestEnemy(new Vector2f(-6, 5)));
        world.spawn(new TestEnemy(new Vector2f(7, 5)));
        world.spawn(new TestEnemy(new Vector2f(6, 6)));
        world.spawn(new TestEnemy(new Vector2f(5, 5)));
        for (int i = 10; i < 15; ++i) {
            for (int j = 10; j < 15; ++j) {
                world.spawn(new TestEnemy(new Vector2f(i, j)));
            }
        }
        return world;
    }
}
