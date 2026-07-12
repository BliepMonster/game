package tiles.crafting.scrolls;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import engine.gui.constraints.size.SizeConstraint;
import main.Main;
import stats.StatHolder;

import java.awt.*;

public class ScrollChoiceButton extends Component {

    public final ScrollApplicationWidget owner;
    public final StatHolder.Stat stat;
    public static final PositionConstraint X_C = new RelativeYConstraint(-.2f);
    public static final SizeConstraint W_C = new RelativeSizeConstraint(.6f);
    public static final SizeConstraint H_C = new RelativeSizeConstraint(.1f);
    public static final PositionConstraint Y1_C = new RelativeYConstraint(-.1f);
    public static final PositionConstraint Y2_C = new RelativeYConstraint(.1f);

    public ScrollChoiceButton(boolean isTop, ScrollApplicationWidget owner, StatHolder.Stat stat) {
        super(X_C, isTop ? Y2_C : Y1_C, W_C, H_C);
        this.owner = owner;
        this.stat = stat;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        boolean canApply = owner.selected != null && owner.selected.canApplyScroll(this.stat, owner.storage);
        draw.rect(canApply ? Color.green : Color.red, x, y, width, height);
        int level = owner.selected != null ? (int) owner.selected.scrolls.stats.get(this.stat) / 3 : -1;
        String s = stat.name().toLowerCase() + " : " + level;
        Font font = Main.font;
        float size = Math.min(font.getSizeForHeight(height), font.getSizeForWidth(s, width));
        draw.string(s, Color.black, x+width/2-font.getWidthForSize(s, size)/2, y, font, size);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y) && owner.selected != null && owner.selected.canApplyScroll(this.stat, owner.storage)) {
            owner.selected.scrolls.apply(this.stat, owner.storage);
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
