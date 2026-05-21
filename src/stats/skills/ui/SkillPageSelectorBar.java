package stats.skills.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import static java.awt.Color.*;

public class SkillPageSelectorBar extends Component {
    private final SkillPanel callback;
    private int current;
    private final int max;
    private final Font font;
    public SkillPageSelectorBar(SkillPanel callback, int max, Font font) {
        super(new CenterConstraint(), new RelativeYConstraint(-.6f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(.2f));
        this.callback = callback;
        this.max = max;
        this.current = 0;
        this.font = font;
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
        if (isInBounds(screen, x, y) && y < -.45f && y > -.55f && x > -.4f && x < .4f) {
            switch (x) {
                case float v when v < -.3f -> callback.switchPage(current - 2);
                case float v when v < -.225f -> {
                    return;
                }
                case float v when v < -.125f -> callback.switchPage(current - 1);
                case float v when v < .125f -> {
                    return;
                }
                case float v when v < .225f -> callback.switchPage(current + 1);
                case float v when v < .3f -> {
                    return;
                }
                default -> callback.switchPage(current + 2);
            }
            current = callback.lp;
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
    // TODO
}
