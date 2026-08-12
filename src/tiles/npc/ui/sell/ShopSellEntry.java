package tiles.npc.ui.sell;

import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import main.GameContext;

import java.awt.*;

public class ShopSellEntry extends Component {
    public final String s;
    public final Font font;
    private int amount;
    private final String name;
    private final int price;
    private final FullItemStorage storage;
    private final GameContext context;
    private static final RelativeYConstraint[] YC = {
            new RelativeYConstraint(.4f),
            new RelativeYConstraint(.2f),
            new RelativeYConstraint(0),
            new RelativeYConstraint(-.2f),
            new RelativeYConstraint(-.4f)
    };
    public ShopSellEntry(ShopSellPanel.Entry entry, Font font, int index, FullItemStorage storage, GameContext context) {
        super(new CenterConstraint(), YC[index], new RelativeSizeConstraint(1f), new RelativeSizeConstraint(.2f));
        this.font = font;
        this.price = entry.value();
        this.name = entry.key();
        this.s = name+": "+price;
        this.amount = entry.amount();
        this.storage = storage;
        this.context = context;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(amount == 0 ? Color.gray : Color.orange, x, y, w, h);
        String str = s+"(you have "+amount+")";
        var fsize = Math.min(font.getSizeForHeight(h), font.getSizeForWidth(str, w));
        draw.string(str, Color.white, x, y, font, fsize);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (amount != 0 && isInBounds(screen, x, y)) {
            storage.currency.get(price);
            storage.storage.spend(name, 1);
            amount--;
            context.getQuests().onSell();
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
