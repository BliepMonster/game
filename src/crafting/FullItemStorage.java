package crafting;

import stats.inventory.CurrencyStorage;
import stats.inventory.items.ItemInventory;
import stats.inventory.resources.InventoryStorage;

public class FullItemStorage {
    public final ItemInventory inventory;
    public final InventoryStorage storage;
    public final CurrencyStorage currency;
    public FullItemStorage(ItemInventory inventory, InventoryStorage storage, CurrencyStorage currency) {
        this.inventory = inventory;
        this.storage = storage;
        this.currency = currency;
    }
}
