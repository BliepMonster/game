package crafting.ui;

import crafting.CraftingRecipe;
import crafting.FullItemStorage;
import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

import java.awt.*;

public class RecipeMenuComponent extends Component {
    private final CraftingRecipe recipe;
    private final GameContext context;
    private final FullItemStorage storage;
    private final Font font;
    private long lastClicked = 0;
    public RecipeMenuComponent(CraftingRecipe recipe, GameContext context, FullItemStorage storage, Font font) {
        super(CenterConstraint.CENTER, new RelativeYConstraint(-.7f), new RelativeSizeConstraint(1f), new RelativeSizeConstraint(1.2f));
        this.recipe = recipe;
        this.context = context;
        this.storage = storage;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLACK, x, y, w, h);
        ItemRequirement[] ireqs = recipe.getRequirements();
        PreCraftRequirement[] preqs = recipe.getPreCraftRequirements();
        float hh = (h-.2f)/(ireqs.length+preqs.length);
        float fsize = Math.min(font.getSizeForHeight(hh), font.getSizeForHeight(.1f));
        hh = font.getHeight(fsize);
        for (int i = 0; i < preqs.length; ++i) {
            draw.string(preqs[i].getRequirementName(), preqs[i].isMet(context) ? Color.white : Color.red, x, y+h-(i+1)*hh, font, fsize);
        }
        for (int i = 0; i < ireqs.length; ++i) {
            draw.string(ireqs[i].getRequirementName(), ireqs[i].isMet(storage) ? Color.white : Color.red, x, y+h-(preqs.length+i+1)*hh, font, fsize);
        }
        draw.rect(System.currentTimeMillis() - lastClicked < 50 ? Color.red : recipe.canCraft(storage, context) ? Color.orange : Color.gray, x, y, w, .2f);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            if (y < -.5f && recipe.canCraft(storage, context)) {
                recipe.craft(storage, context);
                lastClicked = System.currentTimeMillis();
            }
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
