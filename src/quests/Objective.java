package quests;

import main.GameContext;

public abstract class Objective {
    public abstract boolean isDone(GameContext context);
    public abstract int getProgress(GameContext context);
    public abstract int getTotalTasks(GameContext context);
    public abstract String getType();
    public abstract String getName();
}
