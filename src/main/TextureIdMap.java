package main;

import engine.core.Texture;
import engine.core.TextureSlice;
import engine.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class TextureIdMap {
    public static final Texture MISSING = new Texture("res/textures/missing.png");
    private static final HashMap<String, Texture> textures = new HashMap<>();
    public static Texture get(String id) {
        if (textures.containsKey(id))
            return textures.get(id);
        return MISSING;
    }
    public static void put(String id, Texture tex) {
        textures.put(id, tex);
    }
    public static HashMap<String, Texture> getByPrefix(String prefix) {
        var result = new HashMap<String, Texture>();
        for (var entry : textures.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/texids"))) {
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
                                for (var entry : file.values.entrySet()) {
                                    try {
                                        put(entry.getKey(), new Texture(((JsonString) entry.getValue()).value));
                                    } catch (Exception e) {
                                        put(entry.getKey(), MISSING);
                                    }
                                }
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Stream<Path> paths = Files.walk(Path.of("res/slices"))) {
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
                                var dimensions = (JsonArray) file.values.get("dimensions");
                                var x = (int) ((JsonNumber) dimensions.values.getFirst()).num;
                                var y = (int) ((JsonNumber) dimensions.values.get(1)).num;
                                var texid = ((JsonString) file.values.get("texid")).value;
                                var type = file.values.containsKey("type") ? ((JsonString) file.values.get("type")).value : "default";
                                Texture t = get(texid);
                                var texes = TextureSlice.split(t, x, y);
                                if (type.equals("default")) {
                                    var ids = (JsonArray) file.values.get("ids");
                                    for (int i = 0; i < ids.values.size(); ++i) {
                                        var value = ids.values.get(i);
                                        if (value == JsonNull.INSTANCE) continue;
                                        var name = ((JsonString) value).value;
                                        put(name, texes[i]);
                                    }
                                } else if (type.equals("algo")) {
                                    String prefix = ((JsonString) file.values.get("prefix")).value;
                                    String between = ((JsonString) file.values.get("between")).value;
                                    for (int row = 0; row < y; row++) {
                                        for (int col = 0; col < x; col++) {
                                            put(prefix + col + between + row, texes[row * x + col]);
                                        }
                                    }
                                }
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
