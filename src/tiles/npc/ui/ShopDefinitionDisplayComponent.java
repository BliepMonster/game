package tiles.npc.ui;

import crafting.FullItemStorage;
import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import tiles.npc.ShopEntry;

import java.awt.*;

public class ShopDefinitionDisplayComponent extends Component {
    public final Font font;
    public final ShopEntry entry;
    public final ShopBuyButton button;
    public final FullItemStorage storage;
    public final GameContext context;
    public ShopDefinitionDisplayComponent(Font font, ShopEntry entry, FullItemStorage storage, GameContext context) {
        super(new RelativeYConstraint(.25f), new RelativeYConstraint(-.35f), new RelativeSizeConstraint(.5f), new RelativeSizeConstraint(.8f));
        this.entry = entry;
        this.font = font;
        this.button = new ShopBuyButton(entry, context, storage);
        this.context = context;
        this.storage = storage;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        button.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        PreCraftRequirement[] preqs = entry.preCraft();
        ItemRequirement[] ingredients = entry.requirements();
        int size = preqs.length+ingredients.length;
        float hh = Math.min((h-.2f)/size, .1f);
        float hsize = Math.min(font.getSizeForHeight(.2f), font.getSizeForWidth(entry.item().getName(), .5f));
        float fsize = font.getSizeForHeight(hh);
        draw.string(entry.item().getName(), Color.white, x, y+h-.2f, font, hsize);
        float dp = x+h-.2f;
        for (var req : preqs) {
            dp -= hh;
            draw.string(req.getRequirementName(), req.isMet(context) ? Color.white : Color.red, x, dp, font, fsize);
        }
        for (var req : ingredients) {
            dp -= hh;
            draw.string(req.getRequirementName(), req.isMet(storage) ? Color.white : Color.red, x, dp, font, fsize);
        }
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
