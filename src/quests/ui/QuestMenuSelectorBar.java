package quests.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import static java.awt.Color.*;

public class QuestMenuSelectorBar extends Component {
    public final QuestMenuComponent callback;
    private int current = 0;
    public final Font font;
    public final int max;
    public QuestMenuSelectorBar(Font font, QuestMenuComponent callback) {
        super(new CenterConstraint(), new RelativeYConstraint(-.7f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(.2f));
        this.font = font;
        this.callback = callback;
        this.max = callback.quests.size()/5;
        this.current = 0;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        var pos = -.4f;
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
        if (isInBounds(screen, x, y) && y < -.55f && y > -.65f && x > -.4f && x < .4f) {
            switch (x) {
                case float v when v < -.3f -> callback.switchPage(current - 2);
                case float v when v < -.225f -> { return; }
                case float v when v < -.125f -> callback.switchPage(current - 1);
                case float v when v < .125f -> { return; }
                case float v when v < .225f -> callback.switchPage(current + 1);
                case float v when v < .3f -> { return; }
                default -> callback.switchPage(current + 2);
            }
            current = callback.lp;
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
