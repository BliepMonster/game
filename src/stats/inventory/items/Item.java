package stats.inventory.items;

import main.Game;
import saving.SavedItem;

public class Item {
    public int lastUse = 0;
    public final ItemDefinition definition;
    public Item(ItemDefinition definition) {
        this.definition = definition;
    }
    public float getRatioTimeUntilReady() {
        return Math.min((Game.getTickCount()-lastUse)/(float)definition.getCooldownTime(), 1f);
    }
    public Item(SavedItem item) {
        this(ItemDefinition.get(item.id()));
    }
    public SavedItem save() {
        return new SavedItem(
                definition.id
        );
    }
}
