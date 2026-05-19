package engine.gui.constraints.position;

import engine.Maths;
import engine.core.Screen;
import engine.exceptions.InvalidInputException;

public class AbsolutePixelsFromEdgeConstraint implements PositionConstraint {
    public static final int LEFT = 0;
    public static final int BOTTOM = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 3;
    protected int edge, pixels;
    public AbsolutePixelsFromEdgeConstraint(int edge, int pixels) {
        this.edge = edge;
        this.pixels = pixels;
    }
    public float evaluate(Screen screen, float width) {
        if (edge == LEFT) {return -screen.getAspectRatio()+Maths.toGLCoordinates(screen, pixels);}
        else if (edge == RIGHT) {return screen.getAspectRatio()-width-Maths.toGLCoordinates(screen, pixels);}
        else if (edge == TOP) {return 1-width-Maths.toGLCoordinates(screen, pixels);}
        else if (edge == BOTTOM) {return -1+Maths.toGLCoordinates(screen, pixels);}
        else {
            throw new InvalidInputException("Requires LEFT, RIGHT, BOTTOM, or TOP constants as static properties of AbsolutePixelsFromEdgeConstraint or EdgeAlignmentConstraint.");
        }
    }
}
