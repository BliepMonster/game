package engine.gui.constraints.position;

import engine.core.Screen;
import engine.gui.constraints.Constraint;

public interface PositionConstraint extends Constraint {
    float evaluate(Screen screen, float size);
}
