package tiles;

import engine.core.textures.Texture;
import engine.json.*;
import main.Camera;
import main.TextureIdMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class TileType {

    static final HashMap<String, TileType> types = new HashMap<>();
    static final HashMap<TileType, String> keys = new HashMap<>();
    public final boolean standable;
    public final boolean projectileStandable;
    public final Texture texture;
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/tiles"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(string -> {
                                JsonFile file = new JsonParser().parse(string);
                                String id = ((JsonString)file.values.get("id")).value;
                                TileType type = new TileType(
                                        ((JsonBoolean) file.values.get("standable")).bool,
                                        !(file.values.get("projectileStandable") instanceof JsonBoolean projectile) || projectile.bool,
                                        TextureIdMap.get(((JsonString)file.values.get("texture")).value)
                                );
                                types.put(id, type);
                                keys.put(type, id);
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Stream<Path> paths = Files.walk(Path.of("res/special_tiles"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(string -> {
                                JsonFile file = new JsonParser().parse(string);
                                String type = ((JsonString)file.values.get("type")).value;
                                if (type.equals("texture_adapt")) {
                                    boolean b = ((JsonBoolean) file.values.get("standable")).bool;
                                    boolean projectile = !(file.values.get("projectileStandable") instanceof JsonBoolean projectileValue) || projectileValue.bool;
                                    List<String> ids = ((JsonArray) file.values.get("ids")).values.stream().map(json ->
                                            ((JsonString) json).value).toList();
                                    for (String s : ids) {
                                        TileType t = new TileType(b, projectile, TextureIdMap.get(s));
                                        types.put(s, t);
                                        keys.put(t, s);
                                    }
                                } else if (type.equals("prefix_adapt")) {
                                    boolean b = ((JsonBoolean) file.values.get("standable")).bool;
                                    boolean projectile = !(file.values.get("projectileStandable") instanceof JsonBoolean projectileValue) || projectileValue.bool;
                                    String prefix = ((JsonString) file.values.get("prefix")).value;
                                    for (var entry : TextureIdMap.getByPrefix(prefix).entrySet()) {
                                        TileType t = new TileType(b, projectile, entry.getValue());
                                        types.put(entry.getKey(), t);
                                        keys.put(t, entry.getKey());
                                    }
                                }
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static TileType get(String key) {
        return types.get(key);
    }
    public void render(Camera camera, int x, int y) {
        camera.image(texture, x, y, 1, 1);
    }
    public void renderBatched(Camera camera, int x, int y) {
        camera.batchedImage(texture, x, y, 1, 1);
    }
    public void renderPriority(Camera camera, int x, int y, float priority) {
        camera.addRenderJob(() -> camera.image(texture, x, y, 1, 1), priority);
    }
    TileType(boolean isStandable, boolean isProjectileStandable, Texture texture) {
        this.standable = isStandable;
        this.projectileStandable = isProjectileStandable;
        this.texture = texture;
    }
    public JsonValue save() {
        var object = new JsonObject();
        object.values.put("type", new JsonString("default"));
        object.values.put("key", new JsonString(keys.get(this)));
        return object;
    }
}