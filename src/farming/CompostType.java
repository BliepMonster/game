package farming;

import engine.core.textures.Texture;
import engine.json.JsonFile;
import engine.json.JsonNumber;
import engine.json.JsonParser;
import engine.json.JsonString;
import main.TextureIdMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;
/*
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
 */
// "overlay" represents the colored spots in the dirt on both the tile texture and the UI texture
public record CompostType(String id, Texture overlay, String name, float growthBoost, float yieldMultiplier, String compostItem) {
    private static final HashMap<String, CompostType> map = new HashMap<>();
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/compost"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach((str) -> {
                        JsonFile file = new JsonParser().parse(str);
                        var id = ((JsonString) file.values.get("id")).value;
                        var compostItem = ((JsonString) file.values.get("compost_item")).value;
                        map.put(id, new CompostType(id, TextureIdMap.get(((JsonString) file.values.get("overlay")).value), ((JsonString) file.values.get("name")).value, (float) ((JsonNumber) file.values.get("growth")).num, (float) ((JsonNumber) file.values.get("yield")).num, compostItem));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static CompostType get(String id) {
        return map.get(id);
    }
    public static Collection<CompostType> getAll() {
        return map.values();
    }
}
