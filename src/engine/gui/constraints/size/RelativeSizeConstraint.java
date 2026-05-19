package engine.gui.constraints.size;

import engine.core.Screen;

public class RelativeSizeConstraint implements SizeConstraint {
    protected float size;
    public RelativeSizeConstraint(float size) {
        this.size = size;
    }
    public float evaluate(Screen screen) {
        return size;
    }
}
