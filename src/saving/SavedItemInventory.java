package saving;

import combat.equipment.EquipmentSlot;
import engine.saving.objects.*;

import java.util.ArrayList;
import java.util.HashMap;

public record SavedItemInventory(SavedItem[] items, SavedItem[] equipment, EquipmentSlot[] slots) {
    public SavedItemInventory(SavedDictionary object) {
        SavedArray items = (SavedArray) object.get("items");
        var it = new SavedItem[items.length()];
        for (int i = 0; i < items.length(); ++i) {
            var g = items.get(i);
            if (g instanceof SavedNull) it[i] = null;
            else it[i] = new SavedItem(g);
        }
        SavedArray equipment = (SavedArray) object.get("equipment");
        SavedItem[] eq = new SavedItem[equipment.length()];
        EquipmentSlot[] slots = new EquipmentSlot[eq.length];
        for (int i = 0; i < slots.length; ++i) {
            SavedDictionary dict = (SavedDictionary) equipment.get(i);
            SavedObject o = dict.get("item");
            eq[i] = new SavedItem(o);
            slots[i] = EquipmentSlot.values()[
                    (int) ((SavedFloat) dict.get("slot")).getCore()
                    ];
        }
        this(it, eq, slots);
    }
    public static final SavedNull NULL = new SavedNull();
    public SavedDictionary save() {
        ArrayList<SavedObject> saved = new ArrayList<>();
        for (int i = 0; i < items.length; ++i) {
            if (items[i] == null) saved.add(NULL);
            else saved.add(items[i].save());
        }
        var sarr = new SavedArray(saved);
        var objects = new ArrayList<SavedObject>();
        for (int i = 0; i < equipment.length; ++i) {
            var map = new HashMap<String, SavedObject>();
            map.put("item", equipment[i].save());
            map.put("slot", new SavedFloat(slots[i].ordinal()));
            objects.add(new SavedDictionary(map));
        }
        var map = new HashMap<String, SavedObject>();
        map.put("items", sarr);
        map.put("equipment", new SavedArray(objects));
        return new SavedDictionary(map);
    }
}
