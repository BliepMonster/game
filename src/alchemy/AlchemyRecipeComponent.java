package alchemy;

import crafting.FullItemStorage;
import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.Maths;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.core.audio.AudioMaster;
import engine.core.audio.AudioSource;
import engine.core.input.Input;
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
import stats.inventory.items.ui.ItemTooltipComponent;

import java.awt.*;

public class AlchemyRecipeComponent extends Container {
    private static final float HEADER_TOP = -.6f, HEADER_BOTTOM = -.42f;
    public static final int CLICK_BUFFER = AudioMaster.loadSound("res/audio/mixkit-cool-interface-click-tone-2568.wav");
    public static final AudioSource CLICK_SOURCE = new AudioSource().withRelativePositioning(true);
    private final Font font;
    private AlchemyStationPanel panel;
    private AlchemyCraftingRecipe recipe;
    private ItemTooltipComponent tooltip;

    public AlchemyRecipeComponent() {
        super(new Texture("res/textures/gray.png"), CenterConstraint.CENTER, CenterConstraint.CENTER,
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(1.2f));
        this.font = Main.font;
        this.add(new TexturedButton(new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), TextureIdMap.get("base:empty")) {
            @Override
            public void invoke() {
                if (panel.opened > System.currentTimeMillis()-200) return;
                closeTooltip();
                panel.setState(AlchemyStationPanel.STATE_SELECT);
            }
        });
        this.add(new RectangularComponent(Color.red, new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f)));
        this.add(new Label(new RelativeYConstraint(-.4f), new RelativeYConstraint(.5f),
                new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(.1f), "Back", Color.black, Main.font));
    }

    public void setPanel(AlchemyStationPanel panel) {
        this.panel = panel;
    }

    public void setRecipe(AlchemyCraftingRecipe recipe) {
        closeTooltip();
        this.recipe = recipe;
    }

    public AlchemyCraftingRecipe getRecipe() {
        return recipe;
    }

    private boolean canStart(FullItemStorage storage) {
        return !panel.isBusy() && recipe.recipe().canCraft(storage, panel.context);
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        var screen = draw.getScreen();
        float mx = Input.getMouseX(screen);
        float my = Input.getMouseY(screen);
        var item = recipe == null ? null : recipe.recipe().getResultItem();
        if (item == null || !isInBounds(screen, mx, my) || my <= HEADER_BOTTOM || my >= .35f) {
            closeTooltip();
        } else if (tooltip == null) {
            tooltip = new ItemTooltipComponent(item, panel.context);
        }
        if (tooltip != null) tooltip.manage(draw, pressed);
    }

    private void closeTooltip() {
        if (tooltip != null) {
            tooltip.text.close();
            tooltip = null;
        }
    }

    @Override
    public void display(DrawManager draw, float x, float y, float w, float h) {
        super.display(draw, x, y, w, h);
        if (recipe == null || panel == null) return;
        var storage = panel.storage();
        draw.rect(canStart(storage) ? Color.orange : Color.gray, x, HEADER_TOP, w, HEADER_BOTTOM-HEADER_TOP);
        String start = "Start brewing";
        float startSize = Math.min(font.getSizeForWidth(start, w), font.getSizeForHeight(HEADER_BOTTOM-HEADER_TOP));
        float startWidth = font.getWidthForSize(start, startSize);
        draw.string(start, Color.black, x+w/2-startWidth/2, HEADER_TOP+(HEADER_BOTTOM-HEADER_TOP-font.getHeight(startSize))/2, font, startSize);

        draw.image(recipe.icon(), x+w/2-.15f, -.32f, .3f, .3f);

        String info = "Time: "+Maths.countdownString(recipe.time())+"  XP: "+recipe.xp();
        float infoSize = Math.min(font.getSizeForWidth(info, w-.1f), font.getSizeForHeight(.07f));
        float infoWidth = font.getWidthForSize(info, infoSize);
        draw.string(info, Color.black, x+w/2-infoWidth/2, .01f, font, infoSize);

        ItemRequirement[] ireqs = recipe.recipe().getRequirements();
        PreCraftRequirement[] preqs = recipe.recipe().getPreCraftRequirements();
        float ry = .13f;
        for (PreCraftRequirement preq : preqs) {
            ry = drawRequirement(draw, x, w, ry, preq.getRequirementName(), preq.isMet(panel.context) ? Color.white : Color.red);
        }
        for (ItemRequirement ireq : ireqs) {
            ry = drawRequirement(draw, x, w, ry, ireq.getRequirementName(), ireq.isMet(storage) ? Color.white : Color.red);
        }
    }

    private float drawRequirement(DrawManager draw, float x, float w, float ry, String name, Color color) {
        float fsize = Math.min(font.getSizeForHeight(.07f), font.getSizeForWidth(name, w-.12f));
        draw.string(name, color, x+.06f, ry, font, fsize);
        return ry+.07f;
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (recipe == null || panel == null) return;
        if (!isInBounds(screen, x, y)) return;
        if (y >= HEADER_TOP && y <= HEADER_BOTTOM && canStart(panel.storage())) {
            CLICK_SOURCE.play(CLICK_BUFFER);
            panel.startBrewing();
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
