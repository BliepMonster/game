package engine.saving.objects;

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
}
