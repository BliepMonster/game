package stats.inventory.resources;

import main.Main;
import org.jetbrains.annotations.NotNull;
import saving.SavedStorage;
import stats.inventory.resources.ui.DropTrackerComponent;

import java.util.*;

// storage for stackable items
public final class InventoryStorage implements Iterable<Map.Entry<String, Integer>> {
    private final HashMap<String, Integer> inventory = new HashMap<>();
    private final List<String> keys = new ArrayList<>();
    public void add(String name, int count) {
        Main.tracker.add(new DropTrackerComponent.Drop(name, System.currentTimeMillis(), count));
        if (inventory.containsKey(name)) {
            inventory.put(name, inventory.get(name) + count);
        } else {
            keys.add(name);
            inventory.put(name, count);
        }
    }
    public void addSilent(String name, int count) {
        if (inventory.containsKey(name)) {
            inventory.put(name, inventory.get(name) + count);
        } else {
            keys.add(name);
            inventory.put(name, count);
        }
    }
    public void apply(HashMap<String, Integer> loot) {
        for (var entry : loot.entrySet()) this.add(entry.getKey(), entry.getValue());
    }
    public Optional<Integer> get(String name) {
        return Optional.ofNullable(inventory.get(name));
    }
    public boolean contains(String name, int amount) {
        if (inventory.containsKey(name)) {
            return inventory.get(name) >= amount;
        }
        return false;
    }
    public InventoryStorage() {}
    public InventoryStorage(SavedStorage storage) {
        for (var pair : storage.entries()) {
            addSilent(pair.key(), pair.value());
        }
    }
    public InventoryStorage(Map<String, Integer> map) {
        for (var entry : map.entrySet()) this.addSilent(entry.getKey(), entry.getValue());
    }

    // This method does not care whether you have enough. Please check before using this method.
    public void spend(String name, int amount) {
        int n = inventory.get(name) - amount;
        if (n == 0) {
            inventory.remove(name);
            keys.remove(name);
            return;
        }
        inventory.put(name, inventory.get(name) - amount);
    }
    public record InventoryEntry(String name, int amount) {}
    public InventoryEntry get(int n) {
        var e = keys.get(n);
        return new InventoryEntry(e, inventory.get(e));
    }

    @Override
    public @NotNull Iterator<Map.Entry<String, Integer>> iterator() {
        return this.inventory.entrySet().iterator();
    }

    public int size() {
        return inventory.size();
    }
}
