package quests;

import main.GameContext;

public class KillObjective extends Objective {
    private final String enemy;
    private final int goal;
    private final String name;

    public KillObjective(String enemy, int goal, String name) {
        this.enemy = enemy;
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return getProgress(context) >= goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getQuests().getKills(enemy);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public String getType() {
        return "kill";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String display = enemy.endsWith("Enemy") ? enemy.substring(0, enemy.length() - 5) : enemy;
        display = display.replaceAll("([a-z])([A-Z])", "$1 $2");
        return "Kill " + goal + " " + display + (goal > 1 ? "s" : "");
    }
}
