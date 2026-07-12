package stats.inventory.items;

import crafting.FullItemStorage;
import stats.StatHolder;

import java.util.HashMap;

public class ItemScrollSet {
    public final StatHolder stats;

    public static final HashMap<StatHolder.Stat, ScrollItemDefinition> statToScroll = new HashMap<>();
    public static final HashMap<ScrollItemDefinition, StatHolder.Stat> scrollToStat = new HashMap<>();

    public ItemScrollSet(StatHolder stats) {
        this.stats = stats;
    }

    public static ScrollItemDefinition getScroll(StatHolder.Stat stat) {
        return statToScroll.get(stat);
    }

    static {
        for (var def : ItemDefinition.definitions.values()) {
            if (def instanceof ScrollItemDefinition s) {
                statToScroll.put(s.stat, s);
                scrollToStat.put(s, s.stat);
            }
        }
    }

    public void apply(StatHolder.Stat stat, FullItemStorage inv) {
        var scaling = getScroll(stat).scaling;
        var count = (int) (stats.get(stat) / scaling);
        if (count > 5) return;
        var cost = 1 << count;
        inv.storage.spend("Ruby", cost);
        var scrollDef = getScroll(stat);
        for (int i = 0; i < ItemInventory.NUM_ITEMS; i++) {
            var slot = inv.inventory.get(i);
            if (slot != null && slot.definition == scrollDef) {
                inv.inventory.remove(i);
                break;
            }
        }
        stats.with(stat, stats.get(stat) + scaling);
    }

    public StatHolder stats() {
        return stats;
    }
}
