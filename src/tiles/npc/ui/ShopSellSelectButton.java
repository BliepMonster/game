package tiles.npc.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.*;

public class ShopSellSelectButton extends Component {
    public final ShopWrapper wrapper;
    public ShopSellSelectButton(ShopWrapper wrapper) {
        super(new RelativeYConstraint(-.75f), new RelativeYConstraint(.45f), new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f));
        this.wrapper = wrapper;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.green, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) wrapper.gotoSell();
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
