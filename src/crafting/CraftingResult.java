package crafting;

import main.GameContext;
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
    public void produce(FullItemStorage storage, GameContext context) {
        for (Supplier<? extends Item> item : items) {
            var i = item.get();
            context.getQuests().onItemCraft(i.definition.id);
            storage.inventory.add(i);
        }
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            context.getQuests().onResourceCraft(entry.getKey());
            storage.storage.add(entry.getKey(), entry.getValue());
        }
    }
    public boolean canCraft(FullItemStorage storage) {
        return storage.inventory.getRoomLeft() >= items.length;
    }
}
