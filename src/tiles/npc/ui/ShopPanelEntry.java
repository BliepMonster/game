package tiles.npc.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import engine.gui.constraints.size.SizeConstraint;
import tiles.npc.ShopEntry;

import java.awt.*;

public class ShopPanelEntry extends Component {
    public final ShopEntry entry;
    public final Font font;
    public final ShopPanel owner;
    public static final RelativeYConstraint[] XS = {
            new RelativeYConstraint(-.75f),
            new RelativeYConstraint(-.75f+.333333333333333333f),
            new RelativeYConstraint(.25f-.333333333333333333f),
    };
    public static final SizeConstraint  W = new RelativeSizeConstraint(.333333333333333333333333333f),
                                        H = new RelativeSizeConstraint(.4f);
    public static final RelativeYConstraint[] YS = {
            new RelativeYConstraint(-.35f),
            new RelativeYConstraint(.05f),
    };
    public ShopPanelEntry(int px, int py, ShopEntry entry, Font font, ShopPanel owner) {
        super(XS[px], YS[py], W, H);
        this.entry = entry;
        this.font = font;
        this.owner = owner;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.white, x, y, w, h);
        float hh = h/5;
        float ww = w-hh;
        var s = Integer.toString(entry.price().price);
        float fsize = Math.min(font.getSizeForHeight(hh), font.getSizeForWidth(s, ww));
        draw.image(entry.price().currency.getTexture(), x, y, hh, hh);
        draw.string(s, Color.black, x+hh, y, font, fsize);
        var tex = entry.texture();
        draw.image(tex, x, y+hh, w, h-hh);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            owner.selectEntry(entry);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
