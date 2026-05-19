package engine.saving.objects;

public sealed abstract class SavedObject permits SavedBoolean, SavedNull, SavedString, SavedDictionary, SavedArray, SavedFloat {
    public abstract Object get();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SavedObject) {
            if (get() == null)
                return (((SavedObject) obj).get() == null);
            return get().equals(((SavedObject) obj).get());
        } else {
            return false;
        }
    }
}
