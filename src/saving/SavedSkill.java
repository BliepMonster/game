package saving;

import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedFloat;
import engine.saving.objects.SavedObject;

import java.util.HashMap;

public record SavedSkill(int level, int xp) {
    public SavedSkill(SavedObject object) {
        var dict = (SavedDictionary) object;
        this((int) (float) dict.get("level").get(), (int) (float) dict.get("xp").get());
    }
    public SavedObject save() {
        HashMap<String, SavedObject> obj = new HashMap<>();
        obj.put("level", new SavedFloat(level));
        obj.put("xp", new SavedFloat(xp));
        return new SavedDictionary(obj);
    }
}
