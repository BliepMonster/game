package engine.core;

public class TextureSlice extends Texture {
    public TextureSlice(Texture tex, float x, float y, float width, float height) {
        super(tex);
        dimensions[0] = x;
        dimensions[1] = y;
        dimensions[2] = width;
        dimensions[3] = height;
    }
    private final float[] dimensions = new float[4];
    public float[] getDimensions() {
        return dimensions;
    }
    public static TextureSlice[] split(Texture tex, int count) {
        TextureSlice[] slices = new TextureSlice[count*count];
        float ic = 1f/count;
        int index = 0;
        for (float i = 0; i < 1; i += ic) {
            for (float j = 0; j < 1; j += ic) {
                slices[index++] = new TextureSlice(tex, i, j, ic, ic);
            }
        }
        return slices;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof TextureSlice)) return false;
        if (!super.equals(other)) return false;
        var otherCast = (TextureSlice) other;
        if (!java.util.Arrays.equals(this.dimensions, otherCast.dimensions)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + java.util.Arrays.hashCode(dimensions);
        return result;
    }

    @Override
    public String toString() {
        return "TextureSlice[" + "id=" + id + ", " + "width=" + width + ", " + "height=" + height + ", " + "address=" + address + ", " + "dimensions=" + java.util.Arrays.toString(dimensions) + "]";
    }
}
