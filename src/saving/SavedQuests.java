package saving;

import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedNull;
import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;

import java.util.ArrayList;
import java.util.HashMap;

public record SavedQuests(String selectedId, String[] completedIds, String[] claimedIds, HashMap<String, Integer> kills, HashMap<String, Integer> buys, HashMap<String, Integer> gathers, HashMap<String, Integer> talks, int sells, String[] itemCrafts, String[] resourceCrafts) {
    public static SavedQuests empty() {
        return new SavedQuests(null, new String[0], new String[0], new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), 0, new String[0], new String[0]);
    }

    public SavedQuests(SavedObject object) {
        this(
                decodeSelected((SavedDictionary) object),
                decodeCompleted((SavedDictionary) object),
                decodeClaimed((SavedDictionary) object),
                decodeKills((SavedDictionary) object),
                decodeBuys((SavedDictionary) object),
                decodeGathers((SavedDictionary) object),
                decodeTalks((SavedDictionary) object),
                decodeSells((SavedDictionary) object),
                decodeItemCrafts((SavedDictionary) object),
                decodeResourceCrafts((SavedDictionary) object)
        );
    }

    private static String decodeSelected(SavedDictionary dict) {
        SavedObject object = dict.get("selected");
        return object instanceof SavedString string ? (String) string.get() : null;
    }

    private static String[] decodeCompleted(SavedDictionary dict) {
        SavedObject object = dict.get("completed");
        if (!(object instanceof SavedArray array)) return new String[0];
        String[] completed = new String[array.length()];
        for (int i = 0; i < completed.length; ++i) {
            completed[i] = (String) (array.get(i)).get();
        }
        return completed;
    }

    private static String[] decodeClaimed(SavedDictionary dict) {
        SavedObject object = dict.get("claimed");
        if (!(object instanceof SavedArray array)) return new String[0];
        String[] claimed = new String[array.length()];
        for (int i = 0; i < claimed.length; ++i) {
            claimed[i] = (String) (array.get(i)).get();
        }
        return claimed;
    }

    private static String[] decodeItemCrafts(SavedDictionary dict) {
        SavedObject object = dict.get("icrafts");
        if (!(object instanceof SavedArray array)) return new String[0];
        String[] claimed = new String[array.length()];
        for (int i = 0; i < claimed.length; ++i) {
            claimed[i] = (String) ( array.get(i)).get();
        }
        return claimed;
    }

    private static String[] decodeResourceCrafts(SavedDictionary dict) {
        SavedObject object = dict.get("rcrafts");
        if (!(object instanceof SavedArray array)) return new String[0];
        String[] claimed = new String[array.length()];
        for (int i = 0; i < claimed.length; ++i) {
            claimed[i] = (String) ( array.get(i)).get();
        }
        return claimed;
    }

    private static HashMap<String, Integer> decodeKills(SavedDictionary dict) {
        HashMap<String, Integer> kills = new HashMap<>();
        SavedObject object = dict.get("kills");
        if (object instanceof SavedDictionary killsDict) {
            for (var entry : ((HashMap<String, SavedObject>) killsDict.get()).entrySet()) {
                kills.put(entry.getKey(), (int) ((SavedDouble) entry.getValue()).getCore());
            }
        }
        return kills;
    }

    private static HashMap<String, Integer> decodeBuys(SavedDictionary dict) {
        HashMap<String, Integer> buys = new HashMap<>();
        SavedObject object = dict.get("buys");
        if (object instanceof SavedDictionary buysDict) {
            for (var entry : ((HashMap<String, SavedObject>) buysDict.get()).entrySet()) {
                buys.put(entry.getKey(), (int) ((SavedDouble) entry.getValue()).getCore());
            }
        }
        return buys;
    }

    private static HashMap<String, Integer> decodeGathers(SavedDictionary dict) {
        HashMap<String, Integer> gathers = new HashMap<>();
        SavedObject object = dict.get("gathers");
        if (object instanceof SavedDictionary gathersDict) {
            for (var entry : ((HashMap<String, SavedObject>) gathersDict.get()).entrySet()) {
                gathers.put(entry.getKey(), (int) ((SavedDouble) entry.getValue()).getCore());
            }
        }
        return gathers;
    }

    private static HashMap<String, Integer> decodeTalks(SavedDictionary dict) {
        HashMap<String, Integer> talks = new HashMap<>();
        SavedObject object = dict.get("talks");
        if (object instanceof SavedDictionary talksDict) {
            for (var entry : ((HashMap<String, SavedObject>) talksDict.get()).entrySet()) {
                talks.put(entry.getKey(), (int) ((SavedDouble) entry.getValue()).getCore());
            }
        }
        return talks;
    }

    private static int decodeSells(SavedDictionary dict) {
        SavedObject object = dict.get("sells");
        return object instanceof SavedDouble number ? (int) number.getCore() : 0;
    }

    public SavedObject save() {
        HashMap<String, SavedObject> map = new HashMap<>();
        map.put("selected", selectedId == null ? new SavedNull() : new SavedString(selectedId));
        ArrayList<SavedObject> completed = new ArrayList<>();
        for (String id : completedIds) completed.add(new SavedString(id));
        map.put("completed", new SavedArray(completed));
        ArrayList<SavedObject> claimed = new ArrayList<>();
        for (String id : claimedIds) claimed.add(new SavedString(id));
        map.put("claimed", new SavedArray(claimed));
        map.put("kills", encodeCounts(kills));
        map.put("buys", encodeCounts(buys));
        map.put("gathers", encodeCounts(gathers));
        map.put("talks", encodeCounts(talks));
        map.put("sells", new SavedDouble(sells));
        ArrayList<SavedObject> icrafts = new ArrayList<>();
        for (String id : itemCrafts) icrafts.add(new SavedString(id));
        map.put("icrafts", new SavedArray(icrafts));
        ArrayList<SavedObject> rcrafts = new ArrayList<>();
        for (String id : resourceCrafts) rcrafts.add(new SavedString(id));
        map.put("rcrafts", new SavedArray(rcrafts));
        return new SavedDictionary(map);
    }

    private static SavedDictionary encodeCounts(HashMap<String, Integer> counts) {
        HashMap<String, SavedObject> map = new HashMap<>();
        for (var entry : counts.entrySet()) {
            map.put(entry.getKey(), new SavedDouble(entry.getValue()));
        }
        return new SavedDictionary(map);
    }
}
