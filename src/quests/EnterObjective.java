package quests;

import main.GameContext;

public class EnterObjective extends Objective {
    public final String worldID, name;
    public EnterObjective(String worldID, String name) {
        this.worldID = worldID;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return context.getWorld().getID().equals(worldID);
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
        return "enter";
    }

    @Override
    public String getName() {
        return name;
    }
}
