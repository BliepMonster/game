package engine.gui.constraints.size;

import engine.core.Screen;
import engine.gui.constraints.Constraint;

public interface SizeConstraint extends Constraint {
    float evaluate(Screen screen);
}
