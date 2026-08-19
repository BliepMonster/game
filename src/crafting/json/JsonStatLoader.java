package crafting.json;

import engine.json.JsonArray;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;
import stats.StatHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonStatLoader {
    public static StatHolder load(JsonArray array) {
        var holder = new StatHolder();
        for (int i = 0; i < array.values.size(); i++) {
            var obj = (JsonObject) array.values.get(i);
            var type = StatHolder.Stat.valueOf(((JsonString) obj.values.get("type")).value);
            var num = (float) ((JsonNumber) obj.values.get("count")).num;
            holder.with(type, num);
        }
        return holder;
    }
    public static StatHolder load(SavedArray array) {
        var holder = new StatHolder();
        for (int i = 0; i < array.length(); i++) {
            var obj = (SavedDictionary) array.get(i);
            var type = StatHolder.Stat.valueOf((String) obj.get("type").get());
            var num = (float) (double) obj.get("count").get();
            holder.with(type, num);
        }
        return holder;
    }

    public static SavedArray save(StatHolder stats) {
        var entries = new ArrayList<SavedObject>();
        for (var stat : StatHolder.Stat.values()) {
            var v = stats.get(stat);
            if (v == 0) continue;
            var map = new HashMap<String, SavedObject>();
            map.put("type", new SavedString(stat.name()));
            map.put("count", new SavedDouble(v));
            entries.add(new SavedDictionary(map));
        }
        return new SavedArray(entries);
    }
}
