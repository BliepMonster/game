package crafting.json;

import engine.json.JsonArray;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import stats.StatHolder;

public class JsonStatLoader {
    public static StatHolder load(JsonArray array) {
        var holder = new StatHolder();
        for (int i = 0; i < array.values.size(); i++) {
            var obj = (JsonObject) array.values.get(i);
            var type = StatHolder.Stat.valueOf(((JsonString) obj.values.get("type")).value);
            var num = ((JsonNumber) obj.values.get("count")).num;
            holder.with(type, num);
        }
        return holder;
    }
}
