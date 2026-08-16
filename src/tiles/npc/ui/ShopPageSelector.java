package tiles.npc.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import tiles.npc.ui.ShopPanel;

import static java.awt.Color.*;

public class ShopPageSelector extends Component {
    private final int max;
    private int current;
    private final Font font;
    private final ShopPanel callback;
    public ShopPageSelector(Font font, ShopPanel callback) {
        super(new RelativeYConstraint(-.75f), new RelativeYConstraint(-.55f), new RelativeSizeConstraint(1f), new RelativeSizeConstraint(.2f));
        this.font = font;
        this.callback = callback;
        this.max = callback.definition.entries().length/5;
        this.current = 0;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        var pos = -.7f;
        var fsize = font.getSizeForHeight(.1f);
        for (int i = -2; i <= 2; ++i) {
            if (current+i < 0 || current+i > max) {
                pos += .175f;
                continue;
            }
            draw.rect(i == 0 ? magenta : green, pos, y+.05f, .1f, .1f);
            var s = Integer.toString(current+i+1);
            var ww = font.getWidthForSize(s, fsize);
            var cx = pos+.05f-ww/2;
            draw.string(s, black, cx, y+.05f, font, fsize);
            pos += .175f;
        }
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y) && y < -.6f && y > -.7f && x > -.7f && x < 1.2f) {
            if (x < -.6f) {
                callback.switchPage(current - 2);
            } else if (x < -.525f) {
                return;
            } else if (x < -.425f) {
                callback.switchPage(current - 1);
            } else if (x < -.175f) {
                return;
            } else if (x < -.075f) {
                callback.switchPage(current + 1);
            } else if (x < 0f) {
                return;
            } else {
                callback.switchPage(current + 2);
            }
            current = callback.lp;
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
