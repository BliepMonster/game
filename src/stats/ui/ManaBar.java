package stats.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import engine.gui.constraints.size.SizeConstraint;
import entities.Player;
import stats.StatHolder;

import java.awt.*;

public class ManaBar extends Component {
    private final Player player;
    private static final float BAR_WIDTH = .15f;
    private static final float HEALTH_BAR_WIDTH = .2f;

    public ManaBar(Player player) {
        super(new ManaBarXConstraint(), new ManaBarYConstraint(), new ManaBarWidthConstraint(), new RelativeSizeConstraint(.5f));
        this.player = player;
    }

    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLACK, x, y, w, h);
        w *= .8f;
        h *= .8f;
        draw.rect(Color.gray, x, y, w, h);
        if (player.stats == null) return;
        float maxMana = player.stats.get(StatHolder.Stat.MAGIC_POWER);
        if (maxMana <= 0) return;
        h *= (player.getMana() / maxMana);
        draw.rect(Color.blue, x, y, w, h);
    }

    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}

    private static class ManaBarXConstraint implements PositionConstraint {
        public float evaluate(Screen screen, float size) {
            return -screen.getAspectRatio() + HEALTH_BAR_WIDTH;
        }
    }

    private static class ManaBarYConstraint implements PositionConstraint {
        public float evaluate(Screen screen, float size) {
            return -1f;
        }
    }

    private static class ManaBarWidthConstraint implements SizeConstraint {
        public float evaluate(Screen screen) {
            return BAR_WIDTH * screen.getAspectRatio();
        }
    }
}
