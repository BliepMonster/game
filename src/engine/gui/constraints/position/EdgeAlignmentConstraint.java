package engine.gui.constraints.position;

public class EdgeAlignmentConstraint extends AbsolutePixelsFromEdgeConstraint {
    public static final int LEFT = AbsolutePixelsFromEdgeConstraint.LEFT;
    public static final int RIGHT = AbsolutePixelsFromEdgeConstraint.RIGHT;
    public static final int BOTTOM = AbsolutePixelsFromEdgeConstraint.BOTTOM;
    public static final int TOP = AbsolutePixelsFromEdgeConstraint.TOP;
    public EdgeAlignmentConstraint(int edge) {
        super(edge, 0);
    }
}
