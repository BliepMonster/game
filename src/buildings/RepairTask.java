package buildings;

import engine.json.*;
import stats.inventory.resources.InventoryStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record RepairTask(String id, InventoryStorage resources, int coins, String attrib) {
    private static final HashMap<String, RepairTask> tasks = new HashMap<>();
    public static RepairTask get(String task) {
        return tasks.get(task);
    }
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/repair_tasks"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(str -> {
                        JsonFile file = new JsonParser().parse(str);
                        String id = ((JsonString) file.values.get("id")).value;
                        Stream<Map.Entry<String, Integer>> map = ((JsonObject) file.values.get("resources")).values.entrySet().stream()
                                .map(entry -> Map.entry(
                                        entry.getKey(),
                                        (int) ((JsonNumber) entry.getValue()).num));
                        var hash = map.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        tasks.put(id, new RepairTask(id, new InventoryStorage(hash),
                                (int) ((JsonNumber) file.values.get("coins")).num,
                                ((JsonString) file.values.get("attrib")).value
                                ));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
