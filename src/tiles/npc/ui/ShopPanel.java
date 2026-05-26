package tiles.npc.ui;

import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import tiles.npc.ShopEntry;
import tiles.npc.ShopNpcDefinition;
import java.awt.Color;

public class ShopPanel extends Component {
    public final ShopNpcDefinition definition;
    public final Font font;
    public final FullItemStorage storage;
    public final GameContext context;
    public final ShopPanelCloseButton closeButton = new ShopPanelCloseButton(this);
    int lp = 0;
    ShopPanelPage page;
    ShopDefinitionDisplayComponent display;
    public ShopPanel(ShopNpcDefinition definition, Font font, FullItemStorage storage, GameContext context) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(1.5f), new RelativeSizeConstraint(1.1f));
        this.definition = definition;
        this.font = font;
        this.storage = storage;
        this.context = context;
        loadPage(0);
    }
    public void loadPage(int page) {
        lp = page;
        var size = Math.min(definition.entries().length-page*5, 5);
        var entries = new ShopEntry[size];
        System.arraycopy(definition.entries(), page * 5, entries, 0, size);
        var sentries = new ShopPanelEntry[size];
        for (int i = 0; i < size; ++i) {
            sentries[i] = new ShopPanelEntry(
                    i % 3,
                    i % 2,
                    entries[i],
                    font,
                    this
            );
        }
        this.page = new ShopPanelPage(sentries);
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        closeButton.manage(draw, pressed);
        if (display != null) display.manage(draw, pressed);
        page.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.gray, x, y, w*2/3, h-.1f);
        draw.rect(Color.black, x+w*2/3, y, w/3, h-.1f);
        draw.rect(Color.black, x, y+h-.1f, w, .1f);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
    public void close() {
        GUI.scheduleClear(0);
        GUI.toggle(62);
    }
    public void selectEntry(ShopEntry entry) {
        display = new ShopDefinitionDisplayComponent(
                font,
                entry,
                storage,
                context
        );
    }
}
