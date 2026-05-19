package engine.gui.components.shapes;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class RectangularComponent extends Component implements PlainShapeComponent {
    protected Color color;
    public RectangularComponent(Color color, PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        super(x, y, width, height);
        this.color = color;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(color, x, y, width, height);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        //don't use
    }
    public void detectContinuousPress(Screen screen, float x, float y) {
        //don't use
    }
}
