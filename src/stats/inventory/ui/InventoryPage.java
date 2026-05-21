package stats.inventory.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import stats.inventory.InventoryStorage;

import java.awt.*;


public class InventoryPage extends Component {
    private final InventoryItemComponent[] items;
    final Font font;
    public InventoryPage(Font font, InventoryStorage context, int page) {
        super(new CenterConstraint(), new RelativeYConstraint(-.4f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1));
        items = new InventoryItemComponent[5];
        this.font = font;
        for (int i = 0; i < 5; ++i) {
            if (context.size() <= 5*page+i) {
                items[i] = null;
                continue;
            }
            items[i] = new InventoryItemComponent(font, context.get(5*page+i), new CenterConstraint(), new RelativeYConstraint((2-i)*.2f));
        }
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        for (InventoryItemComponent item : items) {
            if (item == null) continue;
            item.manage(draw, pressed);
        }
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.blue, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
