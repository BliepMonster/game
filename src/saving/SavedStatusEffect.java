package saving;

import engine.saving.objects.*;
import stats.StatHolder;

import java.util.HashMap;

public record SavedStatusEffect(int remaining, String uid, StatHolder stats) {
    public SavedStatusEffect(SavedObject object) {
        var dict = (SavedDictionary) object;
        this(
                (int) (float) dict.get("remaining").get(),
                (String) dict.get("uid").get(),
                StatHolderSaver.load((SavedArray) dict.get("stats"))
        );
    }
    public SavedObject save() {
        var dict = new HashMap<String, SavedObject>();
        dict.put("remaining", new SavedFloat(remaining));
        dict.put("uid", new SavedString(uid));
        dict.put("stats", StatHolderSaver.save(stats));
        return new SavedDictionary(dict);
    }
}
