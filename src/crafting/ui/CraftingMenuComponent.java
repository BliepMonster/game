package crafting.ui;

import crafting.CraftingRecipe;
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
import java.util.List;

public class CraftingMenuComponent extends Component {
    public final List<CraftingRecipe> recipes;
    public final FullItemStorage storage;
    public final GameContext context;
    public final CraftingPanel panel;
    public final Font font;
    public int lp = 0;
    private final CraftingMenuSelectorBar selectorBar;
    private CraftingMenuPage page;
    public CraftingMenuComponent(List<CraftingRecipe> recipes, FullItemStorage storage, GameContext context, CraftingPanel panel, Font font) {
        super(CenterConstraint.CENTER, new RelativeYConstraint(-.7f), new RelativeSizeConstraint(1f), new RelativeSizeConstraint(1.2f));
        this.recipes = recipes;
        this.storage = storage;
        this.context = context;
        this.panel = panel;
        this.font = font;
        this.selectorBar = new CraftingMenuSelectorBar(font, this);
        switchPage(0);
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        selectorBar.manage(draw, pressed);
        page.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLACK, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
    public void switchPage(int page) {
        lp = page;
        this.page = new CraftingMenuPage(recipes, lp * 5, storage, context, font, panel);
    }
}
