package main;

import combat.melee.EmptyHitbox;
import engine.ArrayCoordinateMap;
import engine.CoordinateMap;
import engine.core.Screen;
import engine.core.input.Input;
import engine.hitboxes.*;
import engine.json.JsonFile;
import engine.json.JsonNumber;
import engine.json.JsonString;
import entities.Enemy;
import entities.Entity;
import entities.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import tiles.*;
import tiles.resources.ores.CoalOrePart;
import tiles.resources.ores.IronOrePart;
import tool.editor.EditorToolState;
import tool.editor.JSONLoader;

import engine.core.audio.AudioSource;

import java.awt.*;
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
    public final ArrayList<LoopingAudioData> loopingAudioData = new ArrayList<>();
    private final List<Entity> onLoadSpawnedEnemies = new ArrayList<>();
    public final List<AudioSource> loopingSources = new ArrayList<>();
    public final List<Entity> entities = new ArrayList<>();
    private Hitbox hitbox = EmptyHitbox.INSTANCE;
    public float getBrightness() {
        return brightness;
    }
    public World(CoordinateMap<Tile> map, String address, String id) {
        this.map = map;
        this.address = address;
        this.id = id;
        WorldRegistry.put(id, this);
    }
    public void load(Camera camera, Player player) {
        onLoadSpawnedEnemies.clear();
        onLoadEntities.forEach(
                (constructor) -> {
                    try {
                        var entity = constructor.constructor().newInstance(
                                new Vector2f(
                                        constructor.x(),
                                        constructor.y()
                                )
                        );
                        if (entity instanceof Enemy) onLoadSpawnedEnemies.add(entity);
                        entities.add(entity);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        entities.forEach(Entity::onLoad);
        camera.clearLights();
        camera.addLight(player);
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
        for (var data : loopingAudioData) {
            loopingSources.add(data.createSource());
        }
    }
    public void reloadHitbox() {
        var hitbox = new CompoundHitbox();
        for (var tile : map) {
            if (!tile.element().isStandable()) {
                hitbox.add(new RectangularHitbox(tile.x(), tile.y(), 1, 1, Unpositioned.INSTANCE));
            }
        }
        this.hitbox = hitbox;
    }
    int lastHitboxReload = 0;
    public Hitbox getHitbox() {
        if (Game.getTickCount()-lastHitboxReload > 10) {
            lastHitboxReload = Game.getTickCount();
            reloadHitbox();
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
            if (entity.shouldDie(game)) {
                if (entity instanceof Enemy) game.getQuests().onKill(entity.getClass().getSimpleName());
                entity.die(game);
            }
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
            var facing = player.getFacingPosition(new Vector2f(px, py));
            var t = map.get((int) Math.floor(facing.x), (int) Math.floor(facing.y)).orElse(null);
            var used = game.player.useSelectedItem(game, t, new Vector2f(px, py));
            if (t != null && !used) {
                t.onClick(game);
            }
        }
    }
    public static final Color OVERLAY = new Color(0, 0, 0, .5f);
    public static final Color OVERLAY_RED = new Color(1, 0, 0, .5f);
    public void render(Game game, Camera camera, float tsize) {
        var bounds = map.getBounds();
        var minX = bounds.x;
        Screen screen = camera.getDraw().getScreen();
        var player = game.player;
        float px = -screen.getAspectRatio()/tsize+player.getX();
        float py = -1/tsize+player.getY();
        float pw = 2*screen.getAspectRatio()/tsize;
        float ph = 2/tsize;
        minX = Math.max((int) Math.floor(px), minX)-1;
        var xSize = bounds.y;
        xSize = Math.min(xSize, (int) pw)+2;
        var minY = bounds.z;
        minY = Math.max((int) Math.floor(py), minY)-1;
        var ySize = bounds.w;
        ySize = Math.min(ySize, (int) ph)+2;

        for (int i = minX; i <= minX + xSize; ++i) {
            for (int j = minY+ySize; j >= minY; --j) {
                var option = map.get(i, j);
                if (option.isPresent()) {
                    var tile = option.get();
                    if (tile instanceof DefaultTile && tile.getPart() == null) {
                        ((DefaultTile) tile).renderBatched(camera, i, j);
                    }
                }
            }
        }
        camera.flushBatch();
        for (int i = minX; i <= minX + xSize; ++i) {
            for (int j = minY+ySize; j >= minY; --j) {
                var option = map.get(i, j);
                if (option.isPresent()) {
                    var tile = option.get();
                    if (tile instanceof DefaultTile && tile.getPart() == null) continue;
                    tile.render(camera, i, j);
                }
            }
        }
        entities.forEach(entity -> entity.render(camera));
        var vec = player.getFacingPosition(new Vector2f(Input.getMouseX(screen)/tsize+player.getX(), Input.getMouseY(screen)/tsize+player.getY()));
        vec.x = (int) Math.floor(vec.x);
        vec.y = (int) Math.floor(vec.y);
        var part = map.get((int) vec.x, (int)vec.y).orElse(new DefaultTile(TileType.get("GRASS"))).getPart();
        camera.addRenderJob(() -> camera.rect(part instanceof CoalOrePart || part instanceof IronOrePart ? OVERLAY_RED : OVERLAY, vec.x, vec.y, 1, 1), 40000);
    }
    public void spawn(Entity entity) {
        this.toAdd.add(entity);
    }
    public void killOnLoadEnemies() {
        entities.forEach(Entity::onUnload);
        entities.removeAll(onLoadSpawnedEnemies);
        onLoadSpawnedEnemies.clear();
    }
    public void unloadLoopingSources() {
        for (var source : loopingSources) {
            source.stop();
            source.delete();
        }
        loopingSources.clear();
    }
    public void onPlayerRespawn(GameContext context) {
    }
    public static @NotNull World of(JsonFile file, String address, GameContext context) {
        var map = new ArrayCoordinateMap<Tile>();
        JSONLoader.loadWorld(file, map, context);
        var id = ((JsonString) file.values.get("id")).value;
        var type = file.values.get("type") instanceof JsonString ts ? ts.value : "normal";
        World world;
        if ("dungeon".equals(type)) world = new DungeonWorld(map, address, id);
        else world = new World(map, address, id);
        var b = file.values.get("brightness");
        if (b instanceof JsonNumber bn) world.brightness = (float) bn.num;
        try {
            JSONLoader.loadOnSpawn(world, file);
        } catch (Exception _) {
            throw new RuntimeException();
        }
        JSONLoader.loadLoopingAudio(world, file);
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
