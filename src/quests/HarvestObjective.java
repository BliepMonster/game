package quests;

import main.GameContext;

public class HarvestObjective extends Objective {
    private final String item;
    private final int goal;
    private final String name;

    public HarvestObjective(String item, int goal, String name) {
        this.item = item;
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return getProgress(context) >= goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getQuests().getGathers(item);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public String getType() {
        return "harvest";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        return "Harvest " + goal + " " + item;
    }
}
