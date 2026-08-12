package quests;

import main.GameContext;

public class SellObjective extends Objective {
    private final int goal;
    private final String name;

    public SellObjective(int goal, String name) {
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return getProgress(context) >= goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getQuests().getSells();
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public String getType() {
        return "sell";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        return "Sell " + goal + " item" + (goal > 1 ? "s" : "");
    }
}
