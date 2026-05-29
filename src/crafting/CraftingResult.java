package crafting;

import stats.inventory.items.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CraftingResult {
    public final HashMap<String, Integer> resources;
    public final Supplier<Item>[] items;
    public CraftingResult(HashMap<String, Integer> resources, Supplier<Item>[] items) {
        this.resources = resources;
        this.items = items;
    }
    public void produce(FullItemStorage storage) {
        for (Supplier<? extends Item> item : items) {
            storage.inventory.add(item.get());
        }
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            storage.storage.add(entry.getKey(), entry.getValue());
        }
    }
    public boolean canCraft(FullItemStorage storage) {
        return storage.inventory.getRoomLeft() >= items.length;
    }
}
