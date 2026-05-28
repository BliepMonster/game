package main;

import java.util.HashMap;

public class WorldRegistry {
    public static final HashMap<String, World> worlds = new HashMap<>();
    public static World get(String key) {
        return worlds.get(key);
    }
    public static void put(String key, World world) {
        worlds.put(key, world);
    }
}
