package engine.saving.objects;

public final class SavedBoolean extends SavedObject {
    private final boolean bool;
    public SavedBoolean(boolean bool) {
        this.bool = bool;
    }
    public Object get() {
        return bool;
    }
    public boolean getCore() {
        return bool;
    }
    public String toString() {
        return bool+"";
    }
}
