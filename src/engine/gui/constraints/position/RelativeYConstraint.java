package engine.gui.constraints.position;

import engine.core.Screen;

public class RelativeYConstraint implements PositionConstraint {
    protected float relative;
    public RelativeYConstraint(float rel) {
        relative = rel;
    }
    public float evaluate(Screen screen, float size) {
        return relative;
    }
}
