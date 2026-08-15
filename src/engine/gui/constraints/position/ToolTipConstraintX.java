package engine.gui.constraints.position;

import engine.core.Screen;
import engine.core.input.Input;

public class ToolTipConstraintX implements PositionConstraint {
    private final float w;
    public ToolTipConstraintX(float mouseW) {
        this.w = mouseW;
    }

    @Override
    public float evaluate(Screen screen, float size) {
        return Input.getMouseX(screen)+w;
    }
}
