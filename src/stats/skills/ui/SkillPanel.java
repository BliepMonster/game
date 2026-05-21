package stats.skills.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import stats.skills.SkillSet;
import engine.core.text.Font;

import java.awt.Color;

public class SkillPanel extends Component {
    private SkillPage page;
    int lp;
    private final SkillSet set;
    private final SkillPageSelectorBar selector;
    private final Font font;
    public SkillPage loadSkillPage(SkillSet set, int page) {
        int max = page*5+5;
        if (max >= set.size()) max = set.size();
        int min = Math.min(page*5, max);
        SkillComponent[] components = new SkillComponent[max-min];
        for (int i = min; i < max; ++i) {
            components[i-min] = new SkillComponent(set.get(i), new RelativeYConstraint((2-i+min)*.2f), this.font);
        }
        return new SkillPage(components);
    }
    public SkillPanel(SkillSet set, Font font) {
        super(new CenterConstraint(), new CenterConstraint(), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1.2f));
        this.lp = 0;
        this.set = set;
        this.selector = new SkillPageSelectorBar(this, set.size()/5, font);
        this.font = font;
        this.page = loadSkillPage(set, 0);
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        page.manage(draw, pressed);
        selector.manage(draw, pressed);
    }
    public void switchPage(int page) {
        this.page = loadSkillPage(set, page);
        lp = page;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLUE, x, y, w, h);
        page.display(draw, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {
        page.detectClick(screen, x, y);
    }
    public void detectContinuousPress(Screen screen, float x, float y) {
        page.detectContinuousPress(screen, x, y);
    }
}
