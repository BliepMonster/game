package saving;

import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedFloat;
import engine.saving.objects.SavedObject;

import java.util.ArrayList;
import java.util.HashMap;

public record SavedPlayer(
        SavedCurrencyHolder currency,
        SavedItemInventory items, //
        SavedStorage storage,
        SavedStatusEffect[] statusEffects,
        float health,
        SavedSkillSet skills
) {
    static SavedStatusEffect[] decodeStatuses(SavedArray array) {
        SavedStatusEffect[] effects = new SavedStatusEffect[array.length()];
        for (int i = 0; i < effects.length; ++i) {
            effects[i] = new SavedStatusEffect(array.get(i));
        }
        return effects;
    }
    public SavedPlayer(SavedObject object) {
        SavedDictionary dict = (SavedDictionary) object;
        this(
                new SavedCurrencyHolder(dict.get("currency")),
                new SavedItemInventory((SavedDictionary) dict.get("items")),
                new SavedStorage(dict.get("storage")),
                decodeStatuses((SavedArray) dict.get("status")),
                (float) dict.get("hp").get(),
                new SavedSkillSet(dict.get("skills"))
        );
    }
    public SavedObject save() {
        HashMap<String, SavedObject> map = new HashMap<>();
        map.put("currency", currency.save());
        map.put("items", items.save());
        map.put("storage", storage.save());
        SavedArray array = new SavedArray(new ArrayList<>());
        for (var effect : statusEffects) {
            array.add(effect.save());
        }
        map.put("status", array);
        map.put("hp", new SavedFloat(health));
        map.put("skills", skills.save());
        return new SavedDictionary(map);
    }
}
