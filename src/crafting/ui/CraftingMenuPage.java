package crafting.ui;

import crafting.CraftingRecipe;
import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

import java.awt.Color;
import java.util.List;

public class CraftingMenuPage extends Component {
    public final CraftingRecipe[] recipes;
    public final FullItemStorage storage;
    public final GameContext context;
    public final Font font;
    public final CraftingPanel callbackPanel;
    public CraftingMenuPage(List<CraftingRecipe> recipes, int index, FullItemStorage storage, GameContext context, Font font, CraftingPanel panel) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(1), new RelativeSizeConstraint(1));
        var recipesA = new CraftingRecipe[Math.min(5, recipes.size() - index)];
        for (int i = 0; i < recipesA.length; ++i) {
            recipesA[i] = recipes.get(index + i);
        }
        this.recipes = recipesA;
        this.storage = storage;
        this.context = context;
        this.font = font;
        this.callbackPanel = panel;
    }
    public static final float UNIT_SIZE = .2f;
    public void display(DrawManager draw, float x, float y, float w, float h) {
        var yy = y + h;
        draw.rect(Color.black, x, y, w, h);
        for (var recipe : recipes) {
            yy -= UNIT_SIZE;
            draw.rect(recipe.canCraft(storage, context) ? Color.gray : Color.black, x, yy, w, UNIT_SIZE);
            String name = recipe.getName();
            float fsize = Math.min(font.getSizeForWidth(name, w), font.getSizeForHeight(UNIT_SIZE));
            draw.string(name, Color.white, x, yy, font, fsize);
        }
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            int index = (int) ((0.5-y) / UNIT_SIZE);
            if (index >= recipes.length) return;
            var recipe = recipes[index];
            callbackPanel.select(recipe);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
