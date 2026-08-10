package main;

import engine.ArrayCoordinateMap;
import engine.CoordinateMap;
import engine.core.Screen;
import engine.core.input.Input;
import engine.hitboxes.*;
import engine.json.JsonFile;
import engine.json.JsonNumber;
import engine.json.JsonString;
import entities.Entity;
import lighting.LightSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import tiles.*;
import tool.editor.EditorToolState;
import tool.editor.JSONLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class World {
    public final CoordinateMap<Tile> map;
    public final String address;
    final String id;
    private float brightness = 1f;
    public final List<Entity> toAdd = new ArrayList<>();
    public final ArrayList<EntityProducer> onLoadEntities = new ArrayList<>();
    public final List<Entity> entities = new ArrayList<>();
    public float getBrightness() {
        return brightness;
    }
    public World(CoordinateMap<Tile> map, String address, String id) {
        this.map = map;
        this.address = address;
        this.id = id;
        WorldRegistry.put(id, this);
    }
    public void load(Camera camera) {
        onLoadEntities.forEach(
                (constructor) -> {
                    try {
                        entities.add(constructor.constructor().newInstance(
                                new Vector2f(
                                        constructor.x(),
                                        constructor.y()
                                )
                        ));
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        camera.clearLights();
        var mapBounds = map.getBounds();
        var minX = mapBounds.x;
        var xSize = mapBounds.y;
        var minY = mapBounds.z;
        var ySize = mapBounds.w;
        for (int i = minX; i <= minX + xSize; ++i) {
            for (int j = minY; j <= minY + ySize; ++j) {
                map.get(i, j).ifPresent(tile -> {
                    var part = tile.getPart();
                    if (part instanceof LightSource ls) camera.addLight(ls);
                });
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
    public void tick(Game game, boolean pressed, Screen screen, float tsize, boolean canClick) {
        var hitbox = getHitbox();
        entities.forEach(entity -> entity.tick(game));
        entities.forEach(entity -> entity.move(hitbox));
        entities.addAll(toAdd);
        toAdd.clear();
        for (var entity : entities) {
            if (entity.shouldDie(game)) entity.die(game);
        }
        entities.removeIf(entity -> entity.shouldDie(game));
        var player = game.player;
        int left   = (int)Math.floor(player.getX()-.5f);
        int right  = (int)Math.floor((player.getX() + .5f));

        int top    = (int)Math.floor(player.getY()-1);
        int bottom = (int)Math.floor((player.getY() - .8));
        for (int tx = left; tx <= right; tx++) {
            for (int ty = bottom; ty <= top; ty++) {
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
    public void render(Game game, Camera camera, float tsize) {
        var bounds = map.getBounds();
        var minX = bounds.x;
        Screen screen = camera.getDraw().getScreen();
        var player = game.player;
        float px = -screen.getAspectRatio()/tsize+player.getX();
        float py = -1/tsize+player.getY();
        float pw = 2*screen.getAspectRatio()/tsize;
        float ph = 2/tsize;
        minX = Math.max((int) Math.floor(px), minX);
        var xSize = bounds.y;
        xSize = Math.min(xSize, (int) pw);
        var minY = bounds.z;
        minY = Math.max((int) Math.floor(py), minY);
        var ySize = bounds.w;
        ySize = Math.min(ySize, (int) ph);

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
        this.toAdd.add(entity);
    }
    public static @NotNull World of(JsonFile file, String address, GameContext context) {
        var map = new ArrayCoordinateMap<Tile>();
        JSONLoader.loadWorld(file, map, context);
        var world = new World(map, address, ((JsonString) file.values.get("id")).value);
        var b = file.values.get("brightness");
        if (b instanceof JsonNumber bn) world.brightness = bn.num;
        try {
            JSONLoader.loadOnSpawn(world, file);
        } catch (Exception _) {
            throw new RuntimeException();
        }
        return world;
    }
    public void apply(EditorToolState mode, float sx, float sy, Game game, float tsize) {
        int wx = (int)Math.floor(sx / tsize + game.player.getX());
        int wy = (int)Math.floor(sy / tsize + game.player.getY());
        mode.apply(map.get(wx, wy).orElse(null), map, wx, wy, this, game);
    }
    public String getID() {
        return id;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
