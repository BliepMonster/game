package saving;

import crafting.json.JsonStatLoader;
import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;
import stats.StatHolder;

import java.util.HashMap;

public record SavedItem(String id, StatHolder stats) {
    public static SavedItem from(SavedObject object) {
        if (object instanceof SavedString)
            return new SavedItem((String) object.get(), new StatHolder());
        else {
            HashMap<String, SavedObject> map = (HashMap<String, SavedObject>) object.get();
            return new SavedItem((String) map.get("id").get(), JsonStatLoader.load((SavedArray) map.get("stats")));
        }
    }

    public SavedObject save() {
        HashMap<String, SavedObject> map = new HashMap<>();
        SavedDictionary dict = new SavedDictionary(map);
        map.put("id", new SavedString(id));
        map.put("stats", JsonStatLoader.save(stats));
        return dict;
    }
}
