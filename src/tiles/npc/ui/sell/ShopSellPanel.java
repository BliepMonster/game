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
import main.ResourceSellingRegistry;
import tiles.npc.ui.ShopWrapper;

import java.awt.*;
import java.util.ArrayList;

public class ShopSellPanel extends Component {
    public record Entry(String key, int value, int amount) {}
    ArrayList<Entry> sellable;
    final Font font;
    ShopSellPage page;
    private final FullItemStorage storage;
    private final GameContext context;
    int lp = 0;
    private final ShopSellPageSelectorBar bar;
    private final ShopSellRevertButton close;
    public ShopSellPanel(FullItemStorage storage, Font font, ShopWrapper owner, GameContext context) {
        super(CenterConstraint.CENTER, new RelativeYConstraint(-.6f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1.3f));
        this.sellable = new ArrayList<>();
        for (var entry : storage.storage) {
            var v = ResourceSellingRegistry.get(entry.getKey());
            if (v == -1) continue;
            sellable.add(new Entry(entry.getKey(), v, entry.getValue()));
        }
        this.font = font;
        this.storage = storage;
        this.context = context;
        selectPage(0);
        this.close = new ShopSellRevertButton(owner);
        this.bar = new ShopSellPageSelectorBar(font, this);
    }
    public void selectPage(int page) {
        lp = page;
        this.sellable = new ArrayList<>();
        for (var entry : storage.storage) {
            var v = ResourceSellingRegistry.get(entry.getKey());
            if (v == -1) continue;
            sellable.add(new Entry(entry.getKey(), v, entry.getValue()));
        }
        int size = Math.min(sellable.size()-page*5, 5);
        var entries = new Entry[size];
        for (int i = 0; i < size; ++i) {
            entries[i] = sellable.get(i+page*5);
        }
        this.page = new ShopSellPage(
                entries,
                font,
                storage,
                context
        );
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        page.manage(draw, pressed);
        close.manage(draw, pressed);
        bar.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.gray, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
