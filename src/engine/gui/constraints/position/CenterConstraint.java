package engine.gui.constraints.position;

import engine.core.Screen;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.DynamicConstantDesc;
import java.util.Optional;

public class CenterConstraint implements PositionConstraint {
    public static final CenterConstraint CENTER = new CenterConstraint();
    public float evaluate(Screen screen, float width) {
        return -width/2;
    }
}
