package tiles.npc.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class ShopPanelCloseButton extends Component {
    private final ShopPanel panel;
    private static final SizeConstraint SIZE = new RelativeSizeConstraint(.1f);
    private static final PositionConstraint X = new RelativeYConstraint(.65f);
    private static final PositionConstraint Y = new RelativeYConstraint(.45f);
    public ShopPanelCloseButton(ShopPanel panel) {
        super(X, Y, SIZE, SIZE);
        this.panel = panel;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.RED, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            panel.close();
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
