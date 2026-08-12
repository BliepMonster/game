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

public class ShopSellPage extends Component {
    private final ShopSellEntry[] entries;
    public ShopSellPage(ShopSellPanel.Entry[] entries, Font font, FullItemStorage storage, GameContext context) {
        super(new CenterConstraint(), new RelativeYConstraint(-.4f), new RelativeSizeConstraint(1), new RelativeSizeConstraint(1));
        this.entries = new ShopSellEntry[entries.length];
        for (int i = 0; i < entries.length; ++i) {
            this.entries[i] = new ShopSellEntry(
                    entries[i],
                    font,
                    i,
                    storage,
                    context
            );
        }
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        for (var entry : entries) {
            entry.manage(draw, pressed);
        }
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {}
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
