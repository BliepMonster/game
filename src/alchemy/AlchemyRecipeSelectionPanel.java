package alchemy;

import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.Texture;
import engine.core.text.Font;
import engine.gui.components.Container;
import engine.gui.components.TexturedButton;
import engine.gui.components.labels.Label;
import engine.gui.components.shapes.RectangularComponent;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.Main;
import main.TextureIdMap;

import java.awt.*;

public class AlchemyRecipeSelectionPanel extends Container {
    private static final int PER_PAGE = 5;
    private static final float UNIT_SIZE = .16f;
    private static final float LIST_TOP = -.45f, LIST_X = .36f;
    private static final float BAR_Y = .38f;
    private final Font font;
    private AlchemyStationPanel panel;
    private int page = 0;

    public AlchemyRecipeSelectionPanel() {
        super(new Texture("res/textures/gray.png"), CenterConstraint.CENTER, CenterConstraint.CENTER,
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(1.2f));
        this.font = Main.font;
        this.add(new TexturedButton(new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), TextureIdMap.get("base:empty")) {
            @Override
            public void invoke() {
                if (panel.opened > System.currentTimeMillis()-200) return;
                panel.setState(AlchemyStationPanel.STATE_BASE);
            }
        });
        this.add(new RectangularComponent(Color.red, new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f)));
        this.add(new Label(new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), "Back", Color.black, Main.font));
        this.add(new Label(CenterConstraint.CENTER, new RelativeYConstraint(-.57f),
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.1f), "Select recipe", Color.black, Main.font));
    }

    public void setPanel(AlchemyStationPanel panel) {
        this.panel = panel;
    }

    private AlchemyCraftingRecipe[] pageRecipes() {
        var all = AlchemyCraftingRecipe.recipes;
        int from = page*PER_PAGE;
        int count = Math.min(PER_PAGE, Math.max(0, all.length-from));
        var out = new AlchemyCraftingRecipe[count];
        for (int i = 0; i < count; ++i) out[i] = all[from+i];
        return out;
    }

    private int maxPage() {
        return (AlchemyCraftingRecipe.recipes.length-1)/PER_PAGE;
    }

    private boolean startable(AlchemyCraftingRecipe recipe, FullItemStorage storage) {
        return !panel.isBusy() && recipe.recipe().canCraft(storage, panel.context);
    }

    @Override
    public void display(DrawManager draw, float x, float y, float w, float h) {
        super.display(draw, x, y, w, h);
        if (panel == null) return;
        var storage = panel.storage();
        draw.rect(Color.BLACK, x+.04f, LIST_TOP, w-.08f, UNIT_SIZE*PER_PAGE);
        var recipes = pageRecipes();
        for (int i = 0; i < recipes.length; ++i) {
            var recipe = recipes[i];
            float yy = LIST_TOP + i*UNIT_SIZE;
            draw.rect(startable(recipe, storage) ? Color.gray : Color.black, x+.04f, yy, w-.08f, UNIT_SIZE);
            draw.image(recipe.icon(), x+.05f, yy+.02f, UNIT_SIZE-.04f, UNIT_SIZE-.04f);
            String name = recipe.recipe().getName();
            float fsize = Math.min(font.getSizeForWidth(name, w-(UNIT_SIZE+.14f)), font.getSizeForHeight(UNIT_SIZE));
            draw.string(name, Color.white, x+.06f+UNIT_SIZE, yy+(UNIT_SIZE-font.getHeight(fsize))/2, font, fsize);
        }
        drawPageBar(draw);
    }

    private void drawPageBar(DrawManager draw) {
        int max = maxPage();
        float pos = -.4f;
        float fsize = font.getSizeForHeight(.1f);
        for (int i = -2; i <= 2; ++i) {
            if (page+i < 0 || page+i > max) {
                pos += .175f;
                continue;
            }
            draw.rect(i == 0 ? Color.magenta : Color.green, pos, BAR_Y, .1f, .1f);
            String s = Integer.toString(page+i+1);
            float ww = font.getWidthForSize(s, fsize);
            draw.string(s, Color.black, pos+.05f-ww/2, BAR_Y, font, fsize);
            pos += .175f;
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (panel == null) return;
        if (panel.opened > System.currentTimeMillis()-200) return;
        if (!isInBounds(screen, x, y)) return;
        if (handleBarClick(x, y)) return;
        if (panel.isBusy()) return;
        if (x < -LIST_X || x > LIST_X || y < LIST_TOP || y >= LIST_TOP+UNIT_SIZE*PER_PAGE) return;
        int index = (int) ((y-LIST_TOP)/UNIT_SIZE);
        var recipes = pageRecipes();
        if (index >= recipes.length) return;
        panel.select(recipes[index]);
    }

    private boolean handleBarClick(float x, float y) {
        if (y < BAR_Y || y > BAR_Y+.1f || x < -.4f || x > .4f) return false;
        int delta;
        if (x < -.3f) delta = -2;
        else if (x < -.225f) return true;
        else if (x < -.125f) delta = -1;
        else if (x < .125f) return true;
        else if (x < .225f) delta = 1;
        else if (x < .3f) return true;
        else delta = 2;
        int target = page+delta;
        if (target >= 0 && target <= maxPage()) page = target;
        return true;
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
