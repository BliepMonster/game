package tiles.crafting;

import crafting.CraftingRecipe;
import crafting.json.JsonRecipeLoader;
import engine.FileUtils;
import engine.json.JsonArray;
import engine.json.JsonFile;
import engine.json.JsonParser;
import engine.json.JsonString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingStationLoader {
    private static Map<String, CraftingRecipe> cache = new HashMap<>();
    public static List<CraftingRecipe> load(JsonFile file){
        var recipes = (JsonArray) file.values.get("recipes");
        var list = new ArrayList<CraftingRecipe>();
        for (var value : recipes.values) {
            var string = ((JsonString) value).value;
            if (cache.containsKey(string)) {
                list.add(cache.get(string));
            } else {
                var v = JsonRecipeLoader.decodeSimpleJSON(
                        new JsonParser().parse(FileUtils.readAllOrEmpty(string))
                );
                list.add(v);
                cache.put(string, v);
            }
        }
        return list;
    }
}
