package saving;

import engine.saving.objects.*;
import stats.StatHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class StatHolderSaver {
    public static SavedArray save(StatHolder holder) {
        ArrayList<SavedObject> objects = new ArrayList<>();
        for (int i = 0; i < StatHolder.NUM_STATS; ++i) {
            StatHolder.Stat s = StatHolder.Stat.values()[i];
            float stat = holder.get(s);
            if (stat == 0) continue;
            HashMap<String, SavedObject> o = new HashMap<>();
            o.put("type", new SavedString(s.name()));
            o.put("amount", new SavedDouble(holder.getBase(s)));
            if (holder.getAdditive(s) != 0) {
                o.put("additive", new SavedDouble(holder.getAdditive(s)));
            }
            if (holder.getMultiplicative(s) != 1) {
                o.put("multiplicative", new SavedDouble(holder.getMultiplicative(s)));
            }
            objects.add(new SavedDictionary(o));
        }
        return new SavedArray(objects);
    }
    public static StatHolder load(SavedArray array) {
        StatHolder holder = new StatHolder();
        for (SavedObject object : (ArrayList<SavedObject>) array.get()) {
            SavedDictionary dict = (SavedDictionary) object;
            String s = (String) dict.get("type").get();
            StatHolder.Stat stat = StatHolder.Stat.valueOf(s);
            float amount = (float) (double) dict.get("amount").get();
            holder.with(stat, amount);
            var additiveObj = dict.get("additive");
            if (additiveObj != null) {
                holder.withAdditive(stat, (float) (double) additiveObj.get());
            }
            var multiplicativeObj = dict.get("multiplicative");
            if (multiplicativeObj != null) {
                holder.withMultiplicative(stat, (float) (double) multiplicativeObj.get());
            }
        }
        return holder;
    }
}
