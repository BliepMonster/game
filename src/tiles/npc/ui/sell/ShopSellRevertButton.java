package tiles.npc.ui.sell;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import tiles.npc.ui.ShopWrapper;

import java.awt.*;

public class ShopSellRevertButton extends Component {
    public final ShopWrapper wrapper;
    public ShopSellRevertButton(ShopWrapper wrapper) {
        super(new RelativeYConstraint(-.5f), new RelativeYConstraint(.6f), new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f));
        this.wrapper = wrapper;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.RED, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) wrapper.gotoBuy();
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
