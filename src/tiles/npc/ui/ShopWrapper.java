package tiles.npc.ui;

import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import tiles.npc.NpcDefinition;
import tiles.npc.dialogue.DialogueController;
import tiles.npc.dialogue.DialoguePiece;
import tiles.npc.dialogue.ShopDefinition;
import tiles.npc.ui.sell.ShopSellPanel;

public class ShopWrapper extends DialoguePiece {
    enum State {
        SELL,
        BUY
    }
    State state = State.BUY;
    final ShopPanel buy;
    ShopSellPanel sell;
    final FullItemStorage storage;
    final Font font;
    final GameContext context;
    public ShopWrapper(ShopDefinition definition, Font font, FullItemStorage storage, GameContext context, DialogueController controller) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(100), new RelativeSizeConstraint(100), controller); // this doesn't matter here
        buy = new ShopPanel(definition, font, storage, context, this);
        sell = new ShopSellPanel(storage, font, this, context);
        this.storage = storage;
        this.font = font;
        this.context = context;
    }
    public void manage(DrawManager draw, boolean pressed) {
        switch (state) {
            case BUY -> buy.manage(draw, pressed);
            case SELL -> sell.manage(draw, pressed);
        }
    }
    public void gotoSell() {
        state = State.SELL;
        sell = new ShopSellPanel(storage, font, this, context);
    }
    public void gotoBuy() {
        state = State.BUY;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {}
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
