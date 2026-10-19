package quests;

import main.GameContext;

public class AttributeObjective extends Objective {
    private final String attribute;
    private final String name;

    public AttributeObjective(String attribute, String name) {
        this.attribute = attribute;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return context.attributes().contains(attribute);
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
        return "attribute";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        String display = attribute;
        if (display.contains(":")) display = display.substring(display.lastIndexOf(':') + 1);
        display = display.replace('_', ' ');
        return "Attain the " + display + " attribute";
    }
}
