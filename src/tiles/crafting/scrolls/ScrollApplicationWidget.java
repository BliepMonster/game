package tiles.crafting.scrolls;

import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemScrollSet;

import java.awt.*;

public class ScrollApplicationWidget extends Component {

    enum State {
        OPEN,
        SELECTING
    }

    State state = State.OPEN;
    InventoryGridItemSelectorComponent grid;
    final Player player;
    public final ScrollApplicationPageSelectorBar selector;
    public Item selected;
    public final FullItemStorage storage;
    public final CurrentSelectedItemDisplayField display = new CurrentSelectedItemDisplayField(this);
    public final StatHolder.Stat[] scrollableStats;
    public final ScrollChoiceButton[][] buttons;

    public ScrollApplicationWidget(FullItemStorage storage, Player player) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(1f), new RelativeSizeConstraint(.8f));
        this.storage = storage;
        this.player = player;
        this.scrollableStats = ItemScrollSet.statToScroll.keySet().toArray(new StatHolder.Stat[0]);
        if (scrollableStats.length == 0) {
            this.selector = new ScrollApplicationPageSelectorBar(0);
            this.buttons = new ScrollChoiceButton[0][0];
        } else {
            int pages = (scrollableStats.length + 1) / 2;
            this.selector = new ScrollApplicationPageSelectorBar(pages);
            this.buttons = new ScrollChoiceButton[pages][2];
            for (int p = 0; p < pages; p++) {
                buttons[p][0] = new ScrollChoiceButton(true, this, scrollableStats[p * 2]);
                buttons[p][1] = p * 2 + 1 < scrollableStats.length
                        ? new ScrollChoiceButton(false, this, scrollableStats[p * 2 + 1])
                        : null;
            }
        }
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        if (state == State.OPEN) {
            super.manage(draw, pressed);
            display.manage(draw, pressed);
            selector.manage(draw, pressed);
            if (buttons.length == 0) return;
            int page = Math.min(selector.current, buttons.length - 1);
            if (buttons[page][0] != null) buttons[page][0].manage(draw, pressed);
            if (buttons[page][1] != null) buttons[page][1].manage(draw, pressed);
        } else {
            grid.manage(draw, pressed);
        }
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.gray, x, y, width, height);
        draw.rect(Color.red, .4f, .3f, .1f, .1f);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (x > .4f && y > .3f && isInBounds(screen, x, y)) close();
    }
    void close() {
        GUI.scheduleClear(0);
        GUI.toggle(62);
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
