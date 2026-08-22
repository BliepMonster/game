package stats.inventory.items.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;
import stats.inventory.items.Item;

import java.awt.*;

public class ItemDisplayComponent extends Component {
    private Item item;
    private final ItemDisplayClickCallback callback;
    private final Color background;

    public ItemDisplayComponent(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, ItemDisplayClickCallback callback, Color background, Item item) {
        super(x, y, width, height);
        this.callback = callback;
        this.background = background;
        this.item = item;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(background, x, y, width, height);
        if (item != null)
            draw.image(item.definition.texture, x, y, width, height);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y))
            callback.onClick(this);
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
