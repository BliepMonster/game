package stats.inventory.resources.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import stats.inventory.resources.InventoryStorage;

import static java.awt.Color.blue;

public class InventoryPanel extends Component {
    final InventoryStorage context;
    private final InventoryPageSelectorBar pageSelectorBar;
    private InventoryPage currentPage;
    int lp;
    public InventoryPanel(InventoryStorage context, Font font) {
        super(new CenterConstraint(), new CenterConstraint(), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1.2f));
        this.context = context;
        this.pageSelectorBar = new InventoryPageSelectorBar(font, this);
        this.currentPage = new InventoryPage(font, context, 0);
    }
    public void switchPage(int page) {
        this.currentPage = new InventoryPage(this.currentPage.font, context, page);
        lp = page;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        currentPage.manage(draw, pressed);
        pageSelectorBar.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(blue, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
