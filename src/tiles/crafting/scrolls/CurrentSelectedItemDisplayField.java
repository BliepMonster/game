package tiles.crafting.scrolls;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.*;

public class CurrentSelectedItemDisplayField extends Component {

    public final ScrollApplicationWidget widget;

    public CurrentSelectedItemDisplayField(ScrollApplicationWidget owner) {
        super(new RelativeYConstraint(-.45f), CenterConstraint.CENTER, new RelativeSizeConstraint(.2f), new RelativeSizeConstraint(.2f));
        this.widget = owner;
    }
    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.darkGray, x, y, width, height);
        if (widget.selected != null) {
            draw.image(widget.selected.definition.texture, x, y, width, height);
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        widget.grid = new InventoryGridItemSelectorComponent(widget, widget.player);
        widget.state = ScrollApplicationWidget.State.SELECTING;
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
