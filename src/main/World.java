package main;

import engine.ArrayCoordinateMap;
import engine.CoordinateMap;
import engine.core.Screen;
import engine.core.input.Input;
import engine.hitboxes.*;
import engine.json.JsonFile;
import engine.json.JsonString;
import entities.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import tiles.*;
import tool.editor.EditorToolState;
import tool.editor.JSONLoader;

import java.util.ArrayList;
import java.util.List;

public class World {
    final CoordinateMap<Tile> map;
    final String address;
    final String id;
    public final List<Entity> entities = new ArrayList<>();
    public World(CoordinateMap<Tile> map, String address, String id) {
        this.map = map;
        this.address = address;
        this.id = id;
        WorldRegistry.put(id, this);
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
    public void render(Camera camera) {
        var bounds = map.getBounds();
        var minX = bounds.x; // TODO: cull out of screen
        var xSize = bounds.y;
        var minY = bounds.z;
        var ySize = bounds.w;
        for (int i = minX; i <= minX + xSize; ++i) {
            for (int j = minY+ySize; j >= minY; --j) {
                var option = map.get(i, j);
                if (option.isPresent()) {
                    var tile = option.get();
                    tile.render(camera, i, j);
                }
            }
        }
        entities.forEach(entity -> entity.render(camera));
    }
    public void spawn(Entity entity) {
        this.entities.add(entity);
    }
    public static @NotNull World of(JsonFile file, String address, GameContext context) {
        var map = new ArrayCoordinateMap<Tile>();
        JSONLoader.loadWorld(file, map, context);
        return new World(map, address, ((JsonString) file.values.get("id")).value);
    }
    public void apply(EditorToolState mode, float sx, float sy, Game game, float tsize) {
        int wx = (int)Math.floor(sx / tsize + game.player.getX());
        int wy = (int)Math.floor(sy / tsize + game.player.getY());
        mode.apply(map.get(wx, wy).orElse(null), map, wx, wy, game);
    }
    public String getID() {
        return id;
    }
}
