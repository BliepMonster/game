package engine.gui.constraints.position;

import engine.core.Screen;
import engine.core.input.Input;

public class ToolTipConstraintY implements PositionConstraint {
    private final float h;
    public ToolTipConstraintY(float mouseH) {
        this.h = mouseH;
    }

    @Override
    public float evaluate(Screen screen, float size) {
        return Input.getMouseY(screen)-size-h;
    }
}
