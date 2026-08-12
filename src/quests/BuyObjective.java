package quests;

import main.GameContext;
import stats.inventory.items.ItemDefinition;

public class BuyObjective extends Objective {
    private final String itemId;
    private final int goal;
    private final String name;

    public BuyObjective(String itemId, int goal, String name) {
        this.itemId = itemId;
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return getProgress(context) >= goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getQuests().getBuys(itemId);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public String getType() {
        return "buy";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String display = itemId;
        try {
            display = ItemDefinition.get(itemId).name;
        } catch (RuntimeException ignored) {
        }
        return "Buy " + goal + " " + display;
    }
}
