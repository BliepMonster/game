package combat;

import engine.json.*;
import entities.LootPiece;
import entities.LootReward;
import entities.LootTable;
import entities.LootType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public final class LootTableRegistry {
    private static final HashMap<String, LootTable> tables = new HashMap<>();
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/loot/tables"))) {
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
                        JsonArray array = (JsonArray) file.values.get("loot");
                        LootPiece[] pieces = new LootPiece[array.values.size()];
                        int i = 0;
                        for (JsonValue value : array.values) {
                            JsonObject object = (JsonObject) value;
                            LootType type = LootType.valueOf(((JsonString) object.values.get("type")).value);
                            LootReward reward = LootRewardRegistry.get(((JsonString) object.values.get("reward")).value);
                            pieces[i++] = new LootPiece(type, reward);
                        }
                        tables.put(id, new LootTable(pieces));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static LootTable get(String id) {
        return tables.get(id);
    }
}
