package engine.saving.objects;

import engine.json.JsonValue;

public sealed abstract class SavedObject permits SavedBoolean, SavedNull, SavedString, SavedDictionary, SavedArray, SavedDouble, SaveFile {
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
    public abstract JsonValue toJSON();
}
