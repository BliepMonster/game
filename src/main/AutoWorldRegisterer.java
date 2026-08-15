package main;

import engine.json.JsonParser;
import engine.json.JsonString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static main.WorldRegistry.get;
import static main.WorldRegistry.put;

record SP(String s, Path p) {}
public class AutoWorldRegisterer {
    public static void registerAll(GameContext context) {
        try (var paths = Files.walk(Path.of("res/maps"))) {
            paths.filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return new SP(Files.readString(path), path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(sp -> {
                        var v = new JsonParser().parse(sp.s());
                        put(
                                ((JsonString) v.values.get("id")).value,
                                World.of(
                                        v,
                                        sp.p().toString(),
                                        context
                                )
                        );
                    }
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
