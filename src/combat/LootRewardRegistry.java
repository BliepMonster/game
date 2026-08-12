package combat;

import engine.json.*;
import entities.LootReward;
import stats.inventory.items.ItemDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LootRewardRegistry {
    private static final HashMap<String, LootReward> tables = new HashMap<>();
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/loot/rewards"))) {
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
                        JsonArray array = (JsonArray) file.values.get("items");
                        ItemDefinition[] definitions = new ItemDefinition[array.values.size()];
                        int i = 0;
                        for (JsonValue value : array.values) {
                            definitions[i++] = ItemDefinition.get(((JsonString) value).value);
                        }
                        var map = ((JsonObject) file.values.get("resources")).values.entrySet().stream()
                                .map(entry -> Map.entry(entry.getKey(), (int) ((JsonNumber) entry.getValue()).num))
                                .collect(HashMap<String, Integer>::new, (m, entry) -> m.put(entry.getKey(), entry.getValue()), HashMap::putAll);
                        var currency = (int)((JsonNumber) file.values.get("coins")).num;
                        tables.put(id, new LootReward(currency, definitions, map));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static LootReward get(String id) {
        return tables.get(id);
    }
}
