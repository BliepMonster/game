package engine.gui.constraints.position;

import engine.core.Screen;

public class RelativeXConstraint implements PositionConstraint {
    protected float relative;
    public RelativeXConstraint(float relative, Screen screen) {
        this.relative = relative/screen.getAspectRatio();
    }
    public float evaluate(Screen screen, float size) {
        return screen.getAspectRatio()*relative;
    }
}
