package crafting;

import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ItemInventory;

public class ConcreteItemRequirement extends ItemRequirement {
    public final int amount;
    public final ItemDefinition item;
    public ConcreteItemRequirement(ItemDefinition item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public String getRequirementName() {
        return amount+" "+item.name;
    }
    public boolean isMet(FullItemStorage storage) {
        int c = 0;
        for (int i = 0; i < ItemInventory.NUM_ITEMS; i++) {
            if (storage.inventory.get(i).definition == item) { // since definitions are singletons
                c++;
            }
        }
        return c >= amount;
    }
    public void consume(FullItemStorage storage) {
        int c = amount;
        if (c == 0) return;
        for (int i = 0; i < ItemInventory.NUM_ITEMS; i++) {
            if (storage.inventory.get(i).definition == item) {
                c--;
                storage.inventory.remove(i);
                if (c == 0) return;
            }
        }
    }
}
