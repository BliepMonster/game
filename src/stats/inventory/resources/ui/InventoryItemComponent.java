package stats.inventory.resources.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import stats.inventory.resources.InventoryStorage;

import java.awt.*;

public class InventoryItemComponent extends Component {
    private final String name;
    private final int amount;
    private final Font font;
    public InventoryItemComponent(Font font, InventoryStorage.InventoryEntry entry, PositionConstraint x, PositionConstraint y) {
        super(x, y, new RelativeSizeConstraint(1f), new RelativeSizeConstraint(.2f));
        this.name = entry.name();
        this.amount = entry.amount();
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.green, x, y, w, h);
        float fsize = Math.min(font.getSizeForWidth(name, w/2), font.getSizeForHeight(h*2f/3f));
        draw.string(name, Color.BLACK, x, y, font, fsize);
        draw.string(Integer.toString(amount), Color.BLACK, x+w-font.getWidthForSize(Integer.toString(amount), fsize), y, font, fsize);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
