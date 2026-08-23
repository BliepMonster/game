package engine.gui.components;

import engine.core.Screen;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

public abstract class DecorativeComponent extends Component {
    public DecorativeComponent(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        super(x, y, width, height);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {

    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
