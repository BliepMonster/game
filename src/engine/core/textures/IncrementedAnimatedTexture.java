package engine.core.textures;

public class IncrementedAnimatedTexture extends AnimatedTexture {
    private float index;
    public int current() {
        return (int) index;
    }
    public void advance(float amount) {
        index += amount;
        index %= this.array.length;
    }
    public void reset() {
        index = 0;
    }

    public IncrementedAnimatedTexture(Texture[] array) {
        super(array);
    }
}
