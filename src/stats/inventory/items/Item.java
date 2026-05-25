package stats.inventory.items;

import main.Game;

public class Item {
    public int lastUse = 0;
    public final ItemDefinition definition;
    public Item(ItemDefinition definition) {
        this.definition = definition;
    }
    public float getRatioTimeUntilReady() {
        return Math.min((Game.getTickCount()-lastUse)/(float)definition.getCooldownTime(), 1f);
    }
}
