package engine.gui.constraints.position;

import engine.core.Screen;

public class CenterInPositionConstraint implements PositionConstraint {
    private final float pos;
    public CenterInPositionConstraint(float pos) {
        this.pos = pos;
    }
    public float evaluate(Screen screen, float size) {
        return pos-size/2;
    }
}
