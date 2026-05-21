package stats.skills.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import stats.skills.Skill;


import static java.awt.Color.*;

public class SkillComponent extends Component {
    private final int level, xp, req;
    private final boolean isMax;
    private final Font font;
    private final String name;
    public SkillComponent(Skill skill, PositionConstraint y, Font font) {
        super(new CenterConstraint(), y, new RelativeSizeConstraint(1), new RelativeSizeConstraint(.2f));
        this.level = skill.getLevel();
        this.xp = skill.getSubLevelXP();
        this.name = skill.getName();
        this.isMax = skill.isAtMax();
        this.req = skill.getRequiredXP();
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(magenta, x, y, w, h);

        // skill level

        String s = Integer.toString(level);
        float fsize = Math.min(font.getSizeForHeight(.1f), font.getSizeForWidth(s, w/4));
        draw.string(s, black, x+w-font.getWidthForSize(s, fsize), y+.2f/3, font, fsize);
        float nsize = Math.min(font.getSizeForHeight(.1f), font.getSizeForWidth(name, w-font.getWidthForSize(s, fsize)));
        draw.string(name, black, x, y+.2f/3, font, nsize);
        draw.rect(gray, x, y, w, .2f/3);
        draw.rect(isMax ? red : green, x, y, w*xp/req, .2f/3);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
