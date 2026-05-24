package crafting.ui;

import crafting.CraftingRecipe;
import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

import java.awt.Color;
import java.util.List;

public class CraftingPanel extends Component {
    enum State {
        MENU,
        SELECTED
    }
    private final CraftingMenuComponent menu;
    private RecipeMenuComponent recipe;
    private final GameContext context;
    private State state = State.MENU;
    private final Font font;
    private final FullItemStorage storage;
    public static final float HEIGHT = 1.4f;
    public static final float WIDTH = 1;
    public final int uiIndex;
    public CraftingPanel(List<CraftingRecipe> recipes, FullItemStorage storage, GameContext context, Font font, int uiIndex) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(WIDTH), new RelativeSizeConstraint(HEIGHT));
        this.font = font;
        this.storage = storage;
        this.context = context;
        this.uiIndex = uiIndex;
        this.menu = new CraftingMenuComponent(recipes, storage, context, this, font);
    }
    void select(CraftingRecipe recipe) {
        this.recipe = new RecipeMenuComponent(recipe, context, storage, font);
        this.state = State.SELECTED;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        if (state == State.SELECTED) recipe.manage(draw, pressed);
        else if (state == State.MENU) menu.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        draw.rect(Color.red, x, y+h-.2f, .2f, .2f);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            if (y > HEIGHT/2-.2f && x < -WIDTH/2+.2f) {
                if (state == State.SELECTED)
                    state = State.MENU;
                else GUI.scheduleClear(uiIndex);
            }
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
