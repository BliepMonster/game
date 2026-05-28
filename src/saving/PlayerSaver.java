package saving;

import combat.equipment.EquipmentSlot;
import entities.Player;
import stats.StatusEffect;
import stats.inventory.CurrencyStorage;
import stats.inventory.items.Item;
import stats.inventory.items.ItemInventory;
import stats.inventory.resources.InventoryStorage;
import stats.skills.Skill;
import stats.skills.SkillSet;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

public class PlayerSaver {
    public static SavedCurrencyHolder saveCurrency(CurrencyStorage storage) {
        return new SavedCurrencyHolder(
                storage.getCoins()
        );
    }
    public static SavedSkill saveSkill(Skill skill) {
        return new SavedSkill(
                skill.getLevel(),
                skill.getSubLevelXP()
        );
    }
    public static SavedSkillSet saveSkills(SkillSet set) {
        SavedSkill[] skills = new SavedSkill[SkillSet.NUM_SKILLS];
        for (int i = 0; i < skills.length; ++i) {
            skills[i] = saveSkill(set.get(i));
        }
        return new SavedSkillSet(skills);
    }
    public static SavedItemInventory saveItems(ItemInventory inventory) {
        SavedItem[] items = new SavedItem[ItemInventory.NUM_ITEMS];
        for (int i = 0; i < items.length; ++i) {
            if (inventory.get(i) == null) items[i] = null;
            else items[i] = inventory.get(i).save();
        }
        ArrayList<SavedItem> list = new ArrayList<>();
        ArrayList<EquipmentSlot> slots = new ArrayList<>();
        var values = EquipmentSlot.values();
        for (int i = 0; i < values.length; ++i) {
            var v = inventory.getEquipment(values[i]);
            if (v == null) continue;
            list.add(v.save());
            slots.add(values[i]);
        }
        return new SavedItemInventory(
                items,
                list.toArray(new SavedItem[0]),
                slots.toArray(new EquipmentSlot[0])
        );
    }
    public static SavedStorage saveStorage(InventoryStorage storage) {
        ArrayList<SavedStorage.Entry> entries = new ArrayList<>();
        for (var v : storage) {
            entries.add(new SavedStorage.Entry(v.getKey(), v.getValue()));
        }
        return new SavedStorage(
                entries.toArray(new SavedStorage.Entry[]{})
        );
    }
    public static SavedStatusEffect[] saveStatusEffects(HashSet<StatusEffect> effects) {
        ArrayList<SavedStatusEffect> e = new ArrayList<>();
        for (var effect : effects) {
            e.add(effect.save());
        }
        return e.toArray(new SavedStatusEffect[]{});
    }
    public static SavedPlayer savePlayer(Player player) {
        return new SavedPlayer(
                saveCurrency(player.currency),
                saveItems(player.items),
                saveStorage(player.inventory),
                player.getX(),
                player.getY(),
                saveStatusEffects(player.effects),
                player.getHealth(),
                saveSkills(player.skills)
        );
    }
}
