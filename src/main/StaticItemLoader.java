package main;

import crafting.json.JsonItemLoader;
import engine.json.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static stats.inventory.items.ItemDefinition.register;

public class StaticItemLoader {
    public static void initAll() {
        try (Stream<Path> paths = Files.walk(Path.of("res/items"))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(string ->
                            register(
                                    JsonItemLoader.load(
                                            new JsonParser().parse(string)
                                    )
                            )
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
