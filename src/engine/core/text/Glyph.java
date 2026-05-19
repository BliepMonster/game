package engine.core.text;

public class Glyph {
    protected int x, y, width, height, xOffset, yOffset, xAdvance;

    public Glyph(int x, int y, int width, int height, int xOffset, int yOffset, int xAdvance) {
        this.y = y;
        this.x = x;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXAdvance() {
        return xAdvance;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Glyph)) return false;
        if (!super.equals(other)) return false;
        var otherCast = (Glyph) other;
        if (this.x != otherCast.x) return false;
        if (this.y != otherCast.y) return false;
        if (this.width != otherCast.width) return false;
        if (this.height != otherCast.height) return false;
        if (this.xOffset != otherCast.xOffset) return false;
        if (this.yOffset != otherCast.yOffset) return false;
        if (this.xAdvance != otherCast.xAdvance) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Integer.hashCode(x);
        result = 31 * result + Integer.hashCode(y);
        result = 31 * result + Integer.hashCode(width);
        result = 31 * result + Integer.hashCode(height);
        result = 31 * result + Integer.hashCode(xOffset);
        result = 31 * result + Integer.hashCode(yOffset);
        result = 31 * result + Integer.hashCode(xAdvance);
        return result;
    }

    @Override
    public String toString() {
        return "Glyph[" + "x=" + x + ", " + "y=" + y + ", " + "width=" + width + ", " + "height=" + height + ", " + "xOffset=" + xOffset + ", " + "yOffset=" + yOffset + ", " + "xAdvance=" + xAdvance + "]";
    }
}
