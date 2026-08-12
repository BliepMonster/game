package quests;

import main.GameContext;

public class Quest {
    private final String id;
    private final String name;
    private final Objective[] objectives;
    private final ObjectiveReward[] rewards;
    private final String requires;

    public Quest(String id, String name, Objective[] objectives, ObjectiveReward[] rewards, String requires) {
        this.id = id;
        this.name = name;
        this.objectives = objectives;
        this.rewards = rewards;
        this.requires = requires;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Objective[] getObjectives() {
        return objectives;
    }

    public ObjectiveReward[] getRewards() {
        return rewards;
    }

    public String getRequires() {
        return requires;
    }

    public boolean isUnlocked(QuestTracker tracker) {
        return requires == null || tracker.isCompleted(requires);
    }

    public boolean isDone(GameContext context) {
        for (Objective objective : objectives) {
            if (!objective.isDone(context)) return false;
        }
        return true;
    }

    public boolean isStarted(GameContext context) {
        for (Objective objective : objectives) {
            if (objective.getProgress(context) > 0) return true;
        }
        return false;
    }
}
