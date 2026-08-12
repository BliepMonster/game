package quests;

import engine.json.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class QuestRegistry {
    private static final HashMap<String, Quest> quests = new HashMap<>();
    private static final ArrayList<String> order = new ArrayList<>();

    public static void register(Quest quest) {
        if (quests.containsKey(quest.getId())) throw new RuntimeException("Duplicate quest id: " + quest.getId());
        quests.put(quest.getId(), quest);
        order.add(quest.getId());
    }

    public static Quest get(String id) {
        Quest quest = quests.get(id);
        if (quest == null) throw new RuntimeException("Invalid quest id: " + id);
        return quest;
    }

    public static List<Quest> all() {
        ArrayList<Quest> list = new ArrayList<>();
        for (String id : order) list.add(quests.get(id));
        return list;
    }

    public static void registerAll() {
        try (Stream<Path> paths = Files.walk(Path.of("res/quests"))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                try {
                    register(JsonQuestLoader.load(new JsonParser().parse(Files.readString(path).strip())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
