package quests;

import main.GameContext;

public class CraftObjective extends Objective {
    private final String name;
    private final String itemID;
    private final boolean isResource;

    public CraftObjective(String name, String itemID, boolean isResource) {
        this.name = name;
        this.itemID = itemID;
        this.isResource = isResource;
    }

    @Override
    public boolean isDone(GameContext context) {
        if (isResource) return context.getQuests().hasResourceBeenCrafted(itemID);
        else return context.getQuests().hasItemBeenCrafted(itemID);
    }

    @Override
    public int getProgress(GameContext context) {
        return isDone(context) ? 1 : 0;
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return 1;
    }

    @Override
    public String getType() {
        return "craft";
    }

    @Override
    public String getName() {
        return name;
    }
}
