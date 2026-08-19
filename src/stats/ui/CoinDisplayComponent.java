package stats.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.EdgeAlignmentConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import tiles.npc.Currency;

import java.awt.*;

public class CoinDisplayComponent extends Component {
    public final Player player;
    public final Font font;
    public final Currency currency;
    public CoinDisplayComponent(Player player, Font font, Currency currency) {
        super(new EdgeAlignmentConstraint(EdgeAlignmentConstraint.RIGHT), new EdgeAlignmentConstraint(EdgeAlignmentConstraint.TOP), new RelativeSizeConstraint(.3f), new RelativeSizeConstraint(.1f));
        this.player = player;
        this.font = font;
        this.currency = currency;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.image(currency.getTexture(), x+w-h, y, h, h);
        var s = Long.toString(player.currency.get(currency));
        float fsize = Math.min(font.getSizeForHeight(h), font.getSizeForWidth(s, w-h));
        draw.string(s, Color.white, x, y, font, fsize);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
