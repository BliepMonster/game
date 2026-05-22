package stats.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.EdgeAlignmentConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import stats.StatHolder;

import java.awt.*;

public class HealthBar extends Component {
    private final Player player;
    public HealthBar(Player player) {
        super(new EdgeAlignmentConstraint(EdgeAlignmentConstraint.LEFT), new EdgeAlignmentConstraint(EdgeAlignmentConstraint.BOTTOM), new RelativeSizeConstraint(.2f), new RelativeSizeConstraint(.5f));
        this.player = player;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLACK, x, y, w, h);
        w *= .8f;
        h *= .8f;
        draw.rect(Color.gray, x, y, w, h);
        h *= (player.getHealth()/player.stats.get(StatHolder.Stat.MAX_HEALTH));
        draw.rect(Color.green, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
