package crafting;

import engine.json.*;
import tiles.npc.ResourceGiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static tiles.npc.JsonShopDefinitionLoader.loadRGiver;

public class DeconstructorRegistry {
    public static final HashMap<String, ResourceGiver[]> resources = new HashMap<>();
    public static ResourceGiver[] loadRGivers(JsonArray array) {
        var givers = new ResourceGiver[array.values.size()];
        for (int i = 0; i < array.values.size(); i++) {
            givers[i] = loadRGiver((JsonObject) array.values.get(i));
        }
        return givers;
    }
    public static void addEntry(JsonFile file) {
        resources.put(
                ((JsonString) file.values.get("id")).value,
                loadRGivers((JsonArray) file.values.get("results"))
        );
    }
    static {
        try (var paths = Files.walk(Path.of("res/deconstructor"))) {
            paths.filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(string -> addEntry(new JsonParser().parse(string))
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
