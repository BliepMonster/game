package alchemy;

import crafting.FullItemStorage;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.components.StateComponent;
import main.GameContext;
import stats.skills.SkillSet;

public class AlchemyStationPanel extends StateComponent {
    public static final int STATE_BASE = 0, STATE_SELECT = 1, STATE_RECIPE = 2;
    final AlchemyStationTilePart part;
    final GameContext context;
    public final long opened = System.currentTimeMillis();
    private final AlchemyRecipeComponent recipeComponent;

    public AlchemyStationPanel(AlchemyStationTilePart part, GameContext context) {
        this(new BaseAlchemyComponent(), new AlchemyRecipeSelectionPanel(), new AlchemyRecipeComponent(), part, context);
    }
    private AlchemyStationPanel(BaseAlchemyComponent base, AlchemyRecipeSelectionPanel selection, AlchemyRecipeComponent recipe,
                                AlchemyStationTilePart part, GameContext context) {
        super(new Component[]{base, selection, recipe});
        this.part = part;
        this.context = context;
        this.recipeComponent = recipe;
        base.setPanel(this);
        selection.setPanel(this);
        recipe.setPanel(this);
    }

    public FullItemStorage storage() {
        var player = context.getPlayer();
        return new FullItemStorage(player.items, player.inventory, player.currency);
    }

    public boolean isBusy() {
        return part.recipe != null;
    }

    public void select(AlchemyCraftingRecipe recipe) {
        recipeComponent.setRecipe(recipe);
        setState(STATE_RECIPE);
    }

    public void startBrewing() {
        var recipe = recipeComponent.getRecipe();
        if (recipe == null || isBusy()) return;
        var storage = storage();
        if (!recipe.recipe().canCraft(storage, context)) return;
        recipe.recipe().consume(storage);
        part.recipe = recipe;
        part.end = System.currentTimeMillis() + recipe.time();
        setState(STATE_BASE);
    }

    public void claim() {
        var finished = part.recipe;
        if (finished == null || !part.isDone()) return;
        finished.recipe().produce(storage());
        context.getPlayer().skills.giveXP(SkillSet.SkillType.ALCHEMY, finished.xp());
        part.recipe = null;
        part.end = 0;
    }

    public void close() {
        GUI.scheduleClear(0);
        GUI.toggle(62);
    }
}
