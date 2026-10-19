package engine.core.textures;

public abstract class AnimatedTexture extends Texture {
    public final Texture[] array;
    public AnimatedTexture(Texture[] array) {
        super(array[0]);
        this.array = array;
    }

    @Override
    public void bind() {
        array[current()].bind();
    }

    @Override
    public boolean equals(Object o) {
        return array[current()].equals(o);
    }

    @Override
    public int getWidth() {
        return array[current()].getWidth();
    }

    @Override
    public int getID() {
        return array[current()].getID();
    }

    @Override
    public int getHeight() {
        return array[current()].getHeight();
    }

    @Override
    public String getAddress() {
        return array[current()].getAddress();
    }

    @Override
    public float[] getDimensions() {
        return array[current()].getDimensions();
    }
    public abstract int current();

    @Override
    public boolean bindBehaviourEquals(Texture other) {
        return this.equals(other);
    }
}
