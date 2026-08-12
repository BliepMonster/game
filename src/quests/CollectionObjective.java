package quests;

import main.GameContext;
import stats.inventory.items.ItemDefinition;

public class CollectionObjective extends Objective {
    private final String item;
    private final int goal;
    private final String name;

    public CollectionObjective(String item, int goal, String name) {
        this.item = item;
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return context.getPlayer().inventory.contains(item, goal);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getPlayer().inventory.get(item).orElse(0);
    }

    @Override
    public String getType() {
        return "collect";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String display = item;
        try {
            display = ItemDefinition.get(item).name;
        } catch (RuntimeException ignored) {
        }
        return "Collect " + goal + " " + display;
    }
}
