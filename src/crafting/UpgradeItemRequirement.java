package crafting;

import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemInventory;

public class UpgradeItemRequirement extends ItemRequirement {
    public final ItemDefinition item;
    public final int amount;
    private Item captured;
    public String getRequirementName() {
        return amount + " " + item.name;
    }
    public UpgradeItemRequirement(ItemDefinition item, int amount) {
        this.item = item;
        this.amount = amount;
    }
    public boolean isMet(FullItemStorage storage) {
        int c = 0;
        for (int i = 0; i < ItemInventory.NUM_ITEMS; i++) {
            var slot = storage.inventory.get(i);
            if (slot != null && slot.definition == this.item) c++;
        }
        return c >= amount;
    }
    public void consume(FullItemStorage storage) {
        int c = amount;
        for (int i = 0; i < ItemInventory.NUM_ITEMS; i++) {
            var slot = storage.inventory.get(i);
            if (slot == null || slot.definition != this.item) continue;
            if (c == amount) captured = slot;
            storage.inventory.remove(i);
            if (--c == 0) return;
        }
    }
    public Item getCaptured() {
        return captured;
    }
}
