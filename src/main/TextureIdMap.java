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
    private static final HashMap<String, Texture> textures = new HashMap<>();
    public static Texture get(String id) {
        return textures.get(id);
    }
    public static void put(String id, Texture tex) {
        textures.put(id, tex);
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
                                    put(entry.getKey(), new Texture(((JsonString)entry.getValue()).value));
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
                                var ids = (JsonArray) file.values.get("ids");
                                var texid = ((JsonString) file.values.get("texid")).value;
                                Texture t = get(texid);
                                var texes = TextureSlice.split(t, x, y);
                                for (int i = 0; i < ids.values.size(); ++i) {
                                    var value = ids.values.get(i);
                                    if (value == JsonNull.INSTANCE) continue;
                                    var name = ((JsonString) value).value;
                                    put(name, texes[i]);
                                }
                            }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
