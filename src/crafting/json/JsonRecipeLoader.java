package crafting.json;

import crafting.*;
import engine.json.*;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class JsonRecipeLoader {
    private static ItemRequirement loadIReq(JsonObject object) {
        JsonValue typej = object.values.get("type");
        if (!(typej instanceof JsonString typestr)) throw new RuntimeException("required string for type field");
        if (typestr.value.equals("item")) {
            String id = ((JsonString) object.values.get("id")).value;
            int amount = (int) ((JsonNumber) object.values.get("amount")).num;
            return new ConcreteItemRequirement(ItemDefinition.get(id), amount);
        } else if (typestr.value.equals("resource")) {
            String name = ((JsonString) object.values.get("id")).value;
            int amount = (int) ((JsonNumber) object.values.get("amount")).num;
            return new ResourceRequirement(name, amount);
        } else throw new RuntimeException("expected 'item' or 'resource'");
    }
    private static ItemRequirement[] loadIReqs(JsonArray array) {
        var reqs = new ItemRequirement[array.values.size()];
        for (int i = 0; i < array.values.size(); ++i) {
            reqs[i] = loadIReq((JsonObject) array.values.get(i));
        }
        return reqs;
    }
    public static List<ItemCraftingRecipe> decodeAll(JsonFile file) {
        JsonArray array = (JsonArray) file.values.get("recipes");
        var list = new ArrayList<ItemCraftingRecipe>();
        for (var value : array.values) {
            list.add(decodeSimpleJSON((JsonObject)value));
        }
        return list;
    }
    // limited in that it only allows 1 item per object
    private static Supplier<Item> loadItemSupplier(JsonObject object) {
        String id = ((JsonString) object.values.get("id")).value;
        var definition = ItemDefinition.get(id);
        return () -> new Item(definition);
    }
    record ValuePair(HashMap<String, Integer> values) {
        void add(ValuePair other) {
            for (var entry : other.values.entrySet()) {
                values.put(entry.getKey(), values.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
    }
    private static ValuePair loadValuePair(JsonObject object) {
        String id = ((JsonString) object.values.get("id")).value;
        int amount = (int) ((JsonNumber) object.values.get("amount")).num;
        var map = new HashMap<String, Integer>();
        map.put(id, amount);
        return new ValuePair(map);
    }
    private static CraftingResult loadResult(JsonArray array) {
        ValuePair v = new ValuePair(new HashMap<>());
        ArrayList<Supplier<Item>> list = new ArrayList<>();
        for (var value : array.values) {
            var object = (JsonObject) value;
            var type = ((JsonString)object.values.get("type")).value;
            if (type.equals("item")) {
                list.add(loadItemSupplier(object));
            } else if (type.equals("resource"))
                v.add(loadValuePair(object));
            else throw new RuntimeException();
        }
        return new CraftingResult(v.values, list.toArray(new Supplier[0]));
    }
    public static ItemCraftingRecipe decodeSimpleJSON(JsonObject file) {
        JsonValue namej = file.values.get("name");
        if (!(namej instanceof JsonString namestr)) throw new RuntimeException("required string for name");
        String name = namestr.value;
        JsonValue inputsj = file.values.get("inputs");
        if (!(inputsj instanceof JsonArray inputsarr)) throw new RuntimeException("required array for inputs");
        ItemRequirement[] reqs = loadIReqs(inputsarr);
        JsonArray array = (JsonArray) file.values.get("outputs");
        CraftingResult result = loadResult(array);
        // later requirements?
        return new ItemCraftingRecipe(
                reqs,
                result,
                new PreCraftRequirement[0],
                name
        );
    }
    public static ItemCraftingRecipe decodeSimpleJSON(JsonFile file) {
        JsonValue namej = file.values.get("name");
        if (!(namej instanceof JsonString namestr)) throw new RuntimeException("required string for name");
        String name = namestr.value;
        JsonValue inputsj = file.values.get("inputs");
        if (!(inputsj instanceof JsonArray inputsarr)) throw new RuntimeException("required array for inputs");
        ItemRequirement[] reqs = loadIReqs(inputsarr);
        JsonArray array = (JsonArray) file.values.get("outputs");
        CraftingResult result = loadResult(array);
        // later requirements?
        return new ItemCraftingRecipe(
                reqs,
                result,
                new PreCraftRequirement[0],
                name
        );
    }
}
