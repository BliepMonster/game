package saving;

import engine.saving.objects.*;

import java.util.ArrayList;
import java.util.HashMap;

public record SavedStorage(Entry[] entries) {
    public record Entry(String key, int value) {
        public Entry(SavedDictionary dict) {
            this(
                    (String) dict.get("key").get(),
                    (int) (float) dict.get("value").get()
            );
        }
    }
    public SavedStorage(SavedObject object) {
        SavedArray array = (SavedArray) object;
        Entry[] entries = new Entry[array.length()];
        for (int i = 0; i < entries.length; ++i) {
            entries[i] = new Entry((SavedDictionary) array.get(i));
        }
        this(entries);
    }
    public SavedObject save() {
        SavedArray array = new SavedArray(new ArrayList<>());
        for (Entry entry : entries) {
            var map = new HashMap<String, SavedObject>();
            map.put("key", new SavedString(entry.key));
            map.put("value", new SavedFloat(entry.value));
            array.add(new SavedDictionary(map));
        }
        return array;
    }
}
