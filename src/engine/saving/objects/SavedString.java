package engine.saving.objects;

public final class SavedString extends SavedObject {
    private final String string;
    public SavedString(String string) {
        this.string = string;
    }

    public Object get() {
        return string;
    }
    public String toString() {
        return "\""+string+"\"";
    }
}
