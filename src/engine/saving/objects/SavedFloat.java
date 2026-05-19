package engine.saving.objects;

public final class SavedFloat extends SavedObject {
    private final float f;
    public SavedFloat(float f) {
        this.f = f;
    }
    public Object get() {
        return f;
    }
    public float getCore() {
        return f;
    }
}
