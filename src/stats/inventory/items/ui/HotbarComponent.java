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
            if (x < -.55f && x > -.65f) player.selectItem(0);
            else if (x < -.4f && x > -.5f) player.selectItem(1);
            else if (x < -.25f && x > -.35f) player.selectItem(2);
            else if (x < -.1f && x > -.2f) player.selectItem(3);
            else if (x < .05f && x > -.05f) player.selectItem(4);
            else if (x < .2f && x > .1f) player.selectItem(5);
            else if (x < .35f && x > .25f) player.selectItem(6);
            else if (x < .5f && x > .4f) player.selectItem(7);
            else if (x < .65f && x > .55f) player.selectItem(8);
        }
    }
    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
