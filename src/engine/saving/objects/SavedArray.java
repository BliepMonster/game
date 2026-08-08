package engine.saving.objects;

import engine.json.JsonArray;
import engine.json.JsonValue;

import java.util.ArrayList;

public final class SavedArray extends SavedObject {
    private final ArrayList<SavedObject> array;
    public SavedArray(ArrayList<SavedObject> array) {
        this.array = array;
    }
    public Object get() {
        return array;
    }
    public SavedObject get(int index) {
        return array.get(index);
    }
    public void remove(int index) {
        array.remove(index);
    }
    public int length() {
        return array.size();
    }
    public void add(SavedObject object) {
        array.add(object);
    }

    @Override
    public JsonValue toJSON() {
        JsonArray a = new JsonArray();
        for (var o : array) a.values.add(o.toJSON());
        return a;
    }
}
