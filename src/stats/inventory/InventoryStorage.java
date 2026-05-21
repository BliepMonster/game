package stats.inventory;

import java.util.*;

// storage for stackable items
public final class InventoryStorage {
    private final HashMap<String, Integer> inventory = new HashMap<>();
    private final List<String> keys = new ArrayList<>();
    public void add(String name, int count) {
        if (inventory.containsKey(name)) {
            inventory.put(name, inventory.get(name) + count);
        } else {
            keys.add(name);
            inventory.put(name, count);
        }
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
    public int size() {
        return inventory.size();
    }
}
