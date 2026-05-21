package stats.skills.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

public class SkillPage extends Component {
    private final SkillComponent[] components;
    public SkillPage(SkillComponent[] components) {
        super(new CenterConstraint(), new RelativeYConstraint(-.4f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1));
        this.components = components;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        for (SkillComponent component : components) {
            component.manage(draw, pressed);
        }
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {}
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
