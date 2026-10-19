package alchemy;

import crafting.ItemCraftingRecipe;
import crafting.json.JsonRecipeLoader;
import engine.FileUtils;
import engine.core.textures.Texture;
import engine.json.*;
import main.TextureIdMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public record AlchemyCraftingRecipe(String id, ItemCraftingRecipe recipe, long time, int xp, Texture icon) {
    public static final AlchemyCraftingRecipe[] recipes;
    private static final HashMap<String, AlchemyCraftingRecipe> byId = new HashMap<>();
    static {
        try (Stream<Path> paths = Files.walk(Path.of("res/recipes/alchemy"))) {
            recipes = paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            var file = new JsonParser().parse(Files.readString(path));
                            var recipePath = ((JsonString) file.values.get("recipe")).value;
                            var xp = (int) ((JsonNumber) file.values.get("xp")).num;
                            var time = (long) ((JsonNumber) file.values.get("time")).num;
                            String id = path.getFileName().toString().replaceFirst("\\.json$", "");
                            return new AlchemyCraftingRecipe(id,
                                    JsonRecipeLoader.decodeSimpleJSON(new JsonParser().parse(FileUtils.readAllOrEmpty(recipePath))), time, xp,
                                    TextureIdMap.get(((JsonString) file.values.get("tex")).value));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(AlchemyCraftingRecipe[]::new);
            for (AlchemyCraftingRecipe recipe : recipes) {
                byId.put(recipe.id(), recipe);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static AlchemyCraftingRecipe get(String id) {
        return byId.get(id);
    }
}
