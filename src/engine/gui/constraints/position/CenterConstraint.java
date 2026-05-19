package engine.gui.constraints.position;

import engine.core.Screen;

public class CenterConstraint implements PositionConstraint {
    public float evaluate(Screen screen, float width) {
        return -width/2;
    }
}
