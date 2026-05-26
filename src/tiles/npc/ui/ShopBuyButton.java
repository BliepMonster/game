package tiles.npc.ui;

import crafting.FullItemStorage;
import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import tiles.npc.ShopEntry;

import java.awt.*;

public class ShopBuyButton extends Component {
    private final ShopEntry entry;
    private final GameContext context;
    private final FullItemStorage storage;
    public ShopBuyButton(ShopEntry entry, GameContext context, FullItemStorage storage) {
        super(new RelativeYConstraint(.25f), new RelativeYConstraint(-.55f), new RelativeSizeConstraint(.5f), new RelativeSizeConstraint(.2f));
        this.entry = entry;
        this.context = context;
        this.storage = storage;
    }
    private boolean canBuy() {
        for (var req : entry.preCraft()) {
            if (!req.isMet(context)) return false;
        }
        for (ItemRequirement req : entry.requirements()) {
            if (!req.isMet(storage)) return false;
        }
        if (!entry.price().hasEnough(storage)) return false;
        return entry.item().canGrant(storage);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(canBuy() ? Color.ORANGE : Color.GRAY, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y) && canBuy()) {
            entry.price().spend(storage);
            for (int i = 0; i < entry.requirements().length; i++) {
                var req = entry.requirements()[i];
                req.consume(storage);
            }
            entry.item().grant(storage);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
