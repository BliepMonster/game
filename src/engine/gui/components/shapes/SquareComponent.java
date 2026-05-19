package engine.gui.components.shapes;

import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class SquareComponent extends RectangularComponent {
    public SquareComponent(Color color, PositionConstraint x, PositionConstraint y, SizeConstraint size) {
        super(color, x, y, size, size);
    }
}
