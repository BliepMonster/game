package engine.gui.constraints.position;

import engine.core.Screen;

public class RelativeFromEdgeConstraintX implements PositionConstraint {
    private float diff;
    private boolean edge;
    public static final boolean LEFT = false;
    public static final boolean RIGHT = true;
    public RelativeFromEdgeConstraintX(float d, boolean edge) {
        diff = d;
        this.edge = edge;
    }

    @Override
    public float evaluate(Screen screen, float size) {
        if (!edge)
            return diff-screen.getAspectRatio();
        return screen.getAspectRatio()-diff;
    }

    public float getDiff() {
        return diff;
    }

    public void setDiff(float diff) {
        this.diff = diff;
    }

    public boolean isEdge() {
        return edge;
    }

    public void setEdge(boolean edge) {
        this.edge = edge;
    }
}
