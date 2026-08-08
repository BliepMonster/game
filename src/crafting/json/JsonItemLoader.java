package crafting.json;

import combat.abilities.AbilityItemDefinition;
import combat.abilities.AbilityRegistry;
import combat.equipment.EquipmentItemDefinition;
import combat.equipment.EquipmentSlot;
import combat.melee.ConeWeaponDefinition;
import combat.melee.MeleeWeaponItemDefinition;
import combat.melee.SpecificStabWeaponDefinition;
import engine.core.Texture;
import engine.json.*;
import entities.DamageType;
import main.TextureIdMap;
import stats.StatHolder;
import stats.inventory.items.*;

// How would this work with items with custom abilities?

public class JsonItemLoader {
    private static EffectConsumableItemDefinition loadConsumable(JsonFile file, String id, String name, String description, Texture texture) {
        var stats = JsonStatLoader.load((JsonArray) file.values.get("stats"));
        int duration = (int) ((JsonNumber) file.values.get("duration")).num;
        String eid = ((JsonString) file.values.get("eid")).value;
        return new EffectConsumableItemDefinition(
                id,
                name,
                description,
                texture,
                stats,
                duration,
                eid
        );
    }
    private static AbilityItemDefinition loadAbility(JsonFile file, String id, String name, String description, Texture texture) {
        String ability = ((JsonString) file.values.get("ability")).value;
        int cooldown = (int) ((JsonNumber) file.values.get("cooldown")).num;
        var stats = JsonStatLoader.load((JsonArray) file.values.get("stats"));
        return new AbilityItemDefinition(
                id,
                name,
                description,
                texture,
                stats,
                cooldown,
                AbilityRegistry.registry.get(ability)
        );
    }
    private static EquipmentItemDefinition loadEquipment(JsonFile file, String id, String name, String description, Texture texture) {
        String slot = ((JsonString) file.values.get("slot")).value;
        StatHolder stats = JsonStatLoader.load((JsonArray) file.values.get("stats"));
        return new EquipmentItemDefinition(stats, EquipmentSlot.valueOf(slot), id, name, description, texture);
    }
    private static ToolItemDefinition.PowerPair loadPowerPair(JsonObject object) {
        var en = ((JsonString) object.values.get("type")).value;
        int amount = (int) ((JsonNumber) object.values.get("amount")).num;
        return new ToolItemDefinition.PowerPair(ToolItemDefinition.PowerType.valueOf(en), amount);
    }
    private static ToolItemDefinition loadTool(JsonFile file, String id, String name, String description, Texture texture) {
        StatHolder stats = JsonStatLoader.load((JsonArray) file.values.get("stats"));
        int cooldown = (int) ((JsonNumber) file.values.get("cooldown")).num;
        JsonArray object = (JsonArray) file.values.get("power");
        ToolItemDefinition.PowerPair[] pairs = new ToolItemDefinition.PowerPair[object.values.size()];
        for (int i = 0; i < object.values.size(); ++i) {
            pairs[i] = loadPowerPair((JsonObject) object.values.get(i));
        }
        return new ToolItemDefinition(cooldown, id, name, description, texture, stats, pairs);
    }
    private static MeleeWeaponItemDefinition loadMelee(JsonFile file, String id, String name, String description, Texture texture) {
        float knockback = 1;
        if (file.values.containsKey("knockback"))
            knockback = ((JsonNumber) file.values.get("knockback")).num;
        String dmgType = ((JsonString) file.values.get("damage_type")).value;
        StatHolder holder = JsonStatLoader.load((JsonArray) file.values.get("stats"));
        String meleeType = ((JsonString) file.values.get("melee_type")).value;
        int damage = (int) ((JsonNumber) file.values.get("damage")).num;
        int cooldown = (int) ((JsonNumber) file.values.get("cooldown")).num;
        switch (meleeType) {
            case "cone" -> {
                float range = ((JsonNumber) file.values.get("range")).num;
                float degrees = ((JsonNumber) file.values.get("angle")).num;
                return new ConeWeaponDefinition(
                        knockback,
                        DamageType.valueOf(dmgType),
                        damage,
                        range,
                        (float) Math.toRadians(degrees),
                        cooldown,
                        name,
                        description,
                        id,
                        texture,
                        holder
                );
            }
            case "stab" -> {
                return new SpecificStabWeaponDefinition(
                        knockback,
                        damage,
                        DamageType.valueOf(dmgType),
                        cooldown,
                        name,
                        description,
                        id,
                        texture,
                        holder
                );
            }
            default -> throw new RuntimeException(meleeType);
        }
    }
    private static BasicFoodItemDefinition loadFood(JsonFile file, String id, String name, String description, Texture texture) {
        int health = (int) ((JsonNumber) file.values.get("restored")).num;
        return new BasicFoodItemDefinition(id, name, description, texture, health);
    }
    private static ScrollItemDefinition loadScroll(JsonFile file, String id, String name, String description, Texture texture) {
        var stat = StatHolder.Stat.valueOf(((JsonString) file.values.get("stat")).value);
        var scaling = ((JsonNumber) file.values.get("scaling")).num;
        return new ScrollItemDefinition(id, name, description, texture, stat, scaling);
    }
    public static ItemDefinition load(JsonFile file) {
        String id = ((JsonString) file.values.get("id")).value;
        String name = ((JsonString) file.values.get("name")).value;
        String description = "";
        if (file.values.containsKey("description"))
            description = ((JsonString) file.values.get("description")).value;
        String ta = ((JsonString) file.values.get("texture")).value;
        Texture texture = TextureIdMap.get(ta);
        if (texture == null) texture = new Texture(ta);
        String type = ((JsonString) file.values.get("type")).value;
        return switch (type) {
            case "equipment" -> loadEquipment(file, id, name, description, texture);
            case "food" -> loadFood(file, id, name, description, texture);
            case "tool" -> loadTool(file, id, name, description, texture);
            case "melee" -> loadMelee(file, id, name, description, texture);
            case "ability" -> loadAbility(file, id, name, description, texture);
            case "consumable" -> loadConsumable(file, id, name, description, texture);
            case "scroll" -> loadScroll(file, id, name, description, texture);
            default -> throw new RuntimeException(type);
        };
    }
}
