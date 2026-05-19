package engine.gui.constraints.position;

import engine.core.Screen;
import engine.hitboxes.Positioned;

public class FollowConstraintY implements PositionConstraint {
    protected Positioned follows;

    public FollowConstraintY(Positioned follows) {
        this.follows = follows;
    }

    public float evaluate(Screen screen, float width) {
        return follows.getY();
    }
}
