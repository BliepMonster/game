package saving;

import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;

public record SavedItem(String id) {
    public SavedItem(SavedObject object) {
        this((String) object.get());
    }

    public SavedObject save() {
        return new SavedString(id);
    }
}
