package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;

public class HuskComponent extends Component {
    public static final HuskComponent COMPONENT = new HuskComponent();
    private HuskComponent() {
        super(null, null, null, null);
    }
    public void display(DrawManager draw, float dummy1, float dummy2, float dummy3, float dummy4) {}
    public void detectClick(Screen screen, float dummy1, float dummy2) {}
    public void detectContinuousPress(Screen screen, float dummy1, float dummy2) {}
    public void manage(DrawManager draw, boolean pressed) {}
}
