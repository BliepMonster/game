package stats.inventory.items;

import crafting.FullItemStorage;
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
        this(ItemDefinition.get(item.id()), new ItemScrollSet(item.stats()));
    }
    public SavedItem save() {
        return new SavedItem(
                definition.id,
                scrolls.stats()
        );
    }
    public boolean canApplyScroll(StatHolder.Stat stat, FullItemStorage inv) {
        var count = scrolls.stats().get(stat) / ItemScrollSet.getScroll(stat).scaling;
        if (count > 5) {
            return false;
        }
        var cost = 1 << (int) count;
        if (!inv.storage.contains("Ruby", cost)) return false;
        return inv.inventory.contains(ItemScrollSet.getScroll(stat));
    }
}
