package crafting;

import stats.inventory.items.ItemInventory;
import stats.inventory.resources.InventoryStorage;

public class FullItemStorage {
    public final ItemInventory inventory;
    public final InventoryStorage storage;
    public FullItemStorage(ItemInventory inventory, InventoryStorage storage) {
        this.inventory = inventory;
        this.storage = storage;
    }
}
