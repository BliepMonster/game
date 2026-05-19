package engine.gui.constraints.size;

import engine.core.Screen;

public class FullWidthConstraint implements SizeConstraint {
    public float evaluate(Screen screen) {
        return 2* screen.getAspectRatio();
    }
}
