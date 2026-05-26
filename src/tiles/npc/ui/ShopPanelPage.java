package tiles.npc.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

public class ShopPanelPage extends Component {
    private final ShopPanelEntry[] entries;
    public ShopPanelPage(ShopPanelEntry[] entries) {
        super(new RelativeYConstraint(-.75f), new RelativeYConstraint(-.35f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(.8f));
        this.entries = entries;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        for (var entry : entries) {
            entry.manage(draw, pressed);
        }
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {}
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
