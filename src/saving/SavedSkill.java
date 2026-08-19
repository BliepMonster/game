package saving;

import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedObject;

import java.util.HashMap;

public record SavedSkill(int level, int xp) {
    public SavedSkill(SavedObject object) {
        var dict = (SavedDictionary) object;
        this((int) (double) dict.get("level").get(), (int) (double) dict.get("xp").get());
    }
    public SavedObject save() {
        HashMap<String, SavedObject> obj = new HashMap<>();
        obj.put("level", new SavedDouble(level));
        obj.put("xp", new SavedDouble(xp));
        return new SavedDictionary(obj);
    }
}
