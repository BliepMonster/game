package crafting.json;

import crafting.*;
import engine.json.*;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemScrollSet;
import stats.skills.SkillSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class JsonRecipeLoader {
    public static ItemRequirement loadIReq(JsonObject object) {
        JsonValue typej = object.values.get("type");
        if (!(typej instanceof JsonString typestr)) throw new RuntimeException("required string for type field");
        if (typestr.value.equals("item")) {
            String id = ((JsonString) object.values.get("id")).value;
            int amount = (int) ((JsonNumber) object.values.get("amount")).num;
            return new ConcreteItemRequirement(ItemDefinition.get(id), amount);
        } else if (typestr.value.equals("upgrade")) {
            String id = ((JsonString) object.values.get("id")).value;
            int amount = (int) ((JsonNumber) object.values.get("amount")).num;
            return new UpgradeItemRequirement(ItemDefinition.get(id), amount);
        } else if (typestr.value.equals("resource")) {
            String name = ((JsonString) object.values.get("id")).value;
            int amount = (int) ((JsonNumber) object.values.get("amount")).num;
            return new ResourceRequirement(name, amount);
        } else throw new RuntimeException("expected 'item', 'upgrade', or 'resource'");
    }
    public static ItemRequirement[] loadIReqs(JsonArray array) {
        var reqs = new ItemRequirement[array.values.size()];
        for (int i = 0; i < array.values.size(); ++i) {
            reqs[i] = loadIReq((JsonObject) array.values.get(i));
        }
        return reqs;
    }
    private static PreCraftRequirement decodeSkillReq(JsonObject object) {
        return new SkillRequirement(
                SkillSet.SkillType.valueOf(
                        ((JsonString) object.values.get("skill")).value
                ),
                (int) ((JsonNumber) object.values.get("level")).num
        );
    }
    private static PreCraftRequirement decode(JsonObject object) {
        String s;
        return switch(s = ((JsonString) object.values.get("type")).value) {
            case "skill" -> decodeSkillReq(object);
            default -> throw new RuntimeException(s);
        };
    }
    public static PreCraftRequirement[] loadPReqs(JsonArray array) {
        if (array == null) return new PreCraftRequirement[0];
        var reqs = new PreCraftRequirement[array.values.size()];
        for (int i = 0; i < reqs.length; i++) {
            reqs[i] = decode((JsonObject) array.values.get(i));
        }
        return reqs;
    }
    // limited in that it only allows 1 item per object
    private static Supplier<Item> loadItemSupplier(JsonObject object) {
        String id = ((JsonString) object.values.get("id")).value;
        var definition = ItemDefinition.get(id);
        return () -> new Item(definition);
    }
    private static Supplier<Item> loadUpgradeItemSupplier(JsonObject object, UpgradeItemRequirement upgradeReq) {
        String id = ((JsonString) object.values.get("id")).value;
        var definition = ItemDefinition.get(id);
        return () -> {
            var captured = upgradeReq.getCaptured();
            var scrolls = captured != null ? captured.scrolls : new ItemScrollSet(new StatHolder());
            return new Item(definition, scrolls);
        };
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
    private static CraftingResult loadResult(JsonArray array, UpgradeItemRequirement upgradeReq) {
        ValuePair v = new ValuePair(new HashMap<>());
        ArrayList<Supplier<Item>> list = new ArrayList<>();
        for (var value : array.values) {
            var object = (JsonObject) value;
            var type = ((JsonString)object.values.get("type")).value;
            if (type.equals("item")) {
                list.add(upgradeReq != null ? loadUpgradeItemSupplier(object, upgradeReq) : loadItemSupplier(object));
            } else if (type.equals("resource"))
                v.add(loadValuePair(object));
            else throw new RuntimeException();
        }
        return new CraftingResult(v.values, list.toArray(new Supplier[0]));
    }
    public static ItemCraftingRecipe decodeSimpleJSON(JsonFile file) {
        JsonValue namej = file.values.get("name");
        if (!(namej instanceof JsonString namestr)) throw new RuntimeException("required string for name");
        String name = namestr.value;
        JsonValue inputsj = file.values.get("inputs");
        if (!(inputsj instanceof JsonArray inputsarr)) throw new RuntimeException("required array for inputs");
        ItemRequirement[] reqs = loadIReqs(inputsarr);
        UpgradeItemRequirement upgradeReq = null;
        for (var req : reqs) {
            if (req instanceof UpgradeItemRequirement u) {
                upgradeReq = u;
                break;
            }
        }
        PreCraftRequirement[] preqs = loadPReqs(
                (JsonArray) file.values.get("requirements")
        );
        JsonArray array = (JsonArray) file.values.get("outputs");
        CraftingResult result = loadResult(array, upgradeReq);
        // later requirements?
        return new ItemCraftingRecipe(
                reqs,
                result,
                preqs,
                name
        );
    }
}
