package stats.inventory.items;

import main.Game;
import saving.SavedItem;
import stats.StatHolder;

public class Item {
    public int lastUse = 0;
    public final ItemDefinition definition;
    public final ItemScrollSet scrolls;
    public Item(ItemDefinition definition) {
        this(definition, new ItemScrollSet(new StatHolder()));
    }
    public Item(ItemDefinition definition, ItemScrollSet scrolls) {
        this.definition = definition;
        this.scrolls = scrolls;
    }
    public float getRatioTimeUntilReady() {
        return Math.min((Game.getTickCount()-lastUse)/(float)definition.getCooldownTime(), 1f);
    }
    public Item(SavedItem item) {
        this(ItemDefinition.get(item.id()));
    }
    public SavedItem save() {
        return new SavedItem(
                definition.id,
                scrolls.stats()
        );
    }
}
