package quests;

import engine.json.*;
import stats.skills.SkillSet;

public class JsonQuestLoader {
    public static Quest load(JsonFile file) {
        String id = ((JsonString) file.values.get("id")).value;
        String name = ((JsonString) file.values.get("name")).value;
        JsonValue requiresValue = file.values.get("requires");
        String requires = requiresValue instanceof JsonString string ? string.value : null;
        JsonArray array = (JsonArray) file.values.get("objectives");
        Objective[] objectives = new Objective[array.values.size()];
        for (int i = 0; i < objectives.length; ++i) {
            objectives[i] = loadObjective((JsonObject) array.values.get(i));
        }
        ObjectiveReward[] rewards = loadRewards(file.values.get("reward"));
        return new Quest(id, name, objectives, rewards, requires);
    }

    private static Objective loadObjective(JsonObject object) {
        String type = ((JsonString) object.values.get("type")).value;
        String name = loadName(object);
        return switch (type) {
            case "collect" -> new CollectionObjective(
                    ((JsonString) object.values.get("item")).value,
                    (int) ((JsonNumber) object.values.get("amount")).num,
                    name
            );
            case "skill" -> new SkillObjective(
                    SkillSet.SkillType.valueOf(((JsonString) object.values.get("skill")).value),
                    (int) ((JsonNumber) object.values.get("level")).num,
                    name
            );
            case "currency" -> new CurrencyObjective((int) ((JsonNumber) object.values.get("amount")).num, name);
            case "kill" -> new KillObjective(
                    ((JsonString) object.values.get("enemy")).value,
                    (int) ((JsonNumber) object.values.get("amount")).num,
                    name
            );
            case "harvest" -> new HarvestObjective(
                    ((JsonString) object.values.get("item")).value,
                    (int) ((JsonNumber) object.values.get("amount")).num,
                    name
            );
            case "sell" -> new SellObjective((int) ((JsonNumber) object.values.get("amount")).num, name);
            case "buy" -> new BuyObjective(
                    ((JsonString) object.values.get("item")).value,
                    (int) ((JsonNumber) object.values.get("amount")).num,
                    name
            );
            case "talk" -> new TalkObjective(
                    ((JsonString) object.values.get("npc")).value,
                    object.values.get("amount") == null ? 1 : (int) ((JsonNumber) object.values.get("amount")).num,
                    name
            );
            case "attribute" -> new AttributeObjective(
                    ((JsonString) object.values.get("attribute")).value,
                    name
            );
            case "craft" -> new CraftObjective(
                    name,
                    ((JsonString) object.values.get("item")).value,
                    ((JsonBoolean) object.values.get("resource")).bool
            );
            default -> throw new RuntimeException("Unknown objective type: " + type);
        };
    }

    private static String loadName(JsonObject object) {
        JsonValue name = object.values.get("name");
        return name instanceof JsonString string ? string.value : null;
    }

    private static ObjectiveReward[] loadRewards(JsonValue value) {
        if (value == null) return null;
        if (value instanceof JsonArray array) {
            ObjectiveReward[] rewards = new ObjectiveReward[array.values.size()];
            for (int i = 0; i < rewards.length; ++i) {
                rewards[i] = loadReward((JsonObject) array.values.get(i));
            }
            return rewards;
        }
        return new ObjectiveReward[] { loadReward((JsonObject) value) };
    }

    private static ObjectiveReward loadReward(JsonObject reward) {
        String type = ((JsonString) reward.values.get("type")).value;
        return switch (type) {
            case "currency" -> new CurrencyReward((int) ((JsonNumber) reward.values.get("amount")).num);
            case "skill" -> new SkillReward(
                    SkillSet.SkillType.valueOf(((JsonString) reward.values.get("skill")).value),
                    (int) ((JsonNumber) reward.values.get("amount")).num
            );
            case "item" -> new ItemReward(
                    ((JsonString) reward.values.get("item")).value,
                    (int) ((JsonNumber) reward.values.get("amount")).num
            );
            default -> throw new RuntimeException("Unknown reward type: " + type);
        };
    }
}
