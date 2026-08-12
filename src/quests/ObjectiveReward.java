package quests;

import main.GameContext;

public abstract class ObjectiveReward {
    public abstract void grant(GameContext context);
    public abstract String getDescription();
}
