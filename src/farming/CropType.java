package farming;

import combat.LootRewardRegistry;
import combat.LootTableRegistry;
import engine.core.Texture;
import engine.json.JsonBoolean;
import engine.json.JsonNumber;
import engine.json.JsonParser;
import engine.json.JsonString;
import entities.LootReward;
import entities.LootTable;
import main.TextureIdMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

public record CropType(String id, Texture ready, Texture unready, Texture icon, long milliDuration, LootReward guaranteed, LootTable bonus, String cropName, String seedType, boolean getsDestroyed, int exp) {
    private static HashMap<String, CropType> map = new HashMap<>();
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/crops"))) {
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
                        var file = new JsonParser().parse(str);
                        var id = ((JsonString) file.values.get("id")).value;
                        map.put(id, new CropType(
                                id,
                                TextureIdMap.get(((JsonString) file.values.get("ready")).value),
                                TextureIdMap.get(((JsonString) file.values.get("unready")).value),
                                TextureIdMap.get(((JsonString) file.values.get("icon")).value),
                                (long) ((JsonNumber) file.values.get("duration")).num,
                                LootRewardRegistry.get(((JsonString) file.values.get("guaranteed")).value),
                                LootTableRegistry.get(((JsonString) file.values.get("optional")).value),
                                ((JsonString) file.values.get("name")).value, ((JsonString) file.values.get("seed")).value,
                                ((JsonBoolean) file.values.get("destroyed")).bool, (int)((JsonNumber) file.values.get("exp")).num
                            ));
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static CropType get(String id) {
        return map.get(id);
    }
    public static Collection<CropType> getAll() {
        return map.values();
    }
}
