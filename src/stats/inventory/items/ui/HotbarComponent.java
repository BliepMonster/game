package stats.inventory.items.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.EdgeAlignmentConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import stats.inventory.items.Item;

import java.awt.*;

// like all current UI, not intended to be pretty
public class HotbarComponent extends Component {
    private final Player player;
    public HotbarComponent(Player player) {
        super(new CenterConstraint(), new EdgeAlignmentConstraint(EdgeAlignmentConstraint.BOTTOM), new RelativeSizeConstraint(1.4f), new RelativeSizeConstraint(.2f));
        this.player = player;
    }

    public static final Color COOLDOWN_BG = new Color(128, 128, 128, 128);

    @Override
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        int s = player.getSelectedHotbarSlot();
        for (int i = 0; i < 9; ++i) {
            Item item = player.items.get(i);
            float xx = x+.05f*(i+1)+.1f*(i);
            draw.rect(s == i ? Color.cyan : Color.green, xx, y+.05f, .1f, .1f);
            if (item == null) continue;
            float cooldown = item.getRatioTimeUntilReady();
            if (cooldown != 1) {
                draw.rect(COOLDOWN_BG, xx, y+.05f, .1f, .1f);
            }
            draw.image(item.definition.texture, xx, y+.05f, .1f, .1f);
            if (cooldown != 1) {
                draw.rect(Color.red, xx, y+.05f, .1f*cooldown, .02f);
            }
        }
    }
    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y) && y > -.95f && y < -.85f) {
            switch (x) {
                case float f when f < -.55f && f > -.65f -> player.selectItem(0);
                case float f when f < -.4f && f > -.5f -> player.selectItem(1);
                case float f when f < -.25f && f > -.35f -> player.selectItem(2);
                case float f when f < -.1f && f > -.2f -> player.selectItem(3);
                case float f when f < .05f && f > -.05f -> player.selectItem(4);
                case float f when f < .2f && f > .1f -> player.selectItem(5);
                case float f when f < .35f && f > .25f -> player.selectItem(6);
                case float f when f < .5f && f > .4f -> player.selectItem(7);
                case float f when f < .65f && f > .55f -> player.selectItem(8);
                default -> {}
            }
        }
    }
    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
