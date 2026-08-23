package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.FullWidthConstraint;
import engine.gui.constraints.size.SizeConstraint;

public class StateComponent extends Component {
    private int state;
    private static final SizeConstraint dummy = new FullWidthConstraint();
    private final Component[] states;
    public StateComponent(Component[] states) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, dummy, dummy); // dummy constraints
        this.states = states;
    }
    public void setState(int state) {
        if (state >= states.length || state < 0) return;
        this.state = state;
    }
    public int getState() {
        return state;
    }
    public Component getCurrent() {
        return states[state];
    }
    @Override
    public void manage(DrawManager draw, boolean pressed) {
        states[state].manage(draw, pressed);
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {

    }

    @Override
    public void detectClick(Screen screen, float x, float y) {

    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
