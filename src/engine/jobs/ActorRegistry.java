package engine.jobs;

import java.util.HashMap;

public class ActorRegistry {
    private static final HashMap<String, ActorBuilder> builders = new HashMap<>();
    public static void register(String name, ActorBuilder builder) {
        builders.put(name, builder);
    }
    public static ActorBuilder get(String name) {
        return builders.get(name);
    }
}
