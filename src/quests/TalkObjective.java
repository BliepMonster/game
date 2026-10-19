package quests;

import main.GameContext;
import tiles.npc.NpcDefinition;
import tiles.npc.ShopDefinitionRegistry;

public class TalkObjective extends Objective {
    private final String npc;
    private final int goal;
    private final String name;

    public TalkObjective(String npc, int goal, String name) {
        this.npc = npc;
        this.goal = goal;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return getProgress(context) >= goal;
    }

    @Override
    public int getProgress(GameContext context) {
        return context.getQuests().getTalks(npc);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return goal;
    }

    @Override
    public String getType() {
        return "talk";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String display = npc;
        try {
            NpcDefinition definition = ShopDefinitionRegistry.get(npc);
            if (definition != null) display = definition.name();
        } catch (RuntimeException ignored) {
        }
        return "Talk to " + display + (goal > 1 ? " " + goal + " times" : "");
    }
}
