package engine.gui.constraints.size;


import engine.Maths;
import engine.core.Screen;

public class AbsolutePixelSizeConstraint implements SizeConstraint {
    protected int pixels;
    public AbsolutePixelSizeConstraint(int pixels) {
        this.pixels = pixels;
    }
    public float evaluate(Screen screen) {
        return Maths.toGLCoordinates(screen, pixels);
    }
}
