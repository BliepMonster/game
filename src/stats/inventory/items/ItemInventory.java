package stats.inventory.items;

import combat.equipment.EquipmentSlot;
import saving.SavedItemInventory;

import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ItemInventory {
    public static final int NUM_ITEMS_PER_ROW = 9;
    public static final int NUM_ROWS = 5;
    public static final int NUM_ITEMS = NUM_ITEMS_PER_ROW * NUM_ROWS;
    private final EnumMap<EquipmentSlot, Item> equipment = new EnumMap<>(EquipmentSlot.class);
    /*
    In this case, the first 9 represent the hotbar items, while the last 36 are pure 'inventory' items.
    */
    private final Item[] items = new Item[NUM_ITEMS];
    public ItemInventory() {}
    public ItemInventory(SavedItemInventory inventory) {
        for (int i = 0; i < inventory.slots().length; ++i) {
            equipment.put(inventory.slots()[i], new Item(inventory.equipment()[i]));
        }
        for (int i = 0; i < NUM_ITEMS; ++i) {
            if (i >= inventory.items().length) break;
            var item = inventory.items()[i];
            if (item != null) items[i] = new Item(item);
        }
    }
    public boolean add(Item item) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (items[i] == null) {
                items[i] = item;
                return true;
            }
        }
        return false;
    }
    public Item get(int index) {
        return items[index];
    }
    public Stream<Item> stream() {
        return Stream.of(items);
    }
    public boolean contains(ItemDefinition definition) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (items[i] != null && items[i].definition.equals(definition)) {
                return true;
            }
        }
        return false;
    }
    public boolean isFull() {
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (items[i] == null) {
                return false;
            }
        }
        return true;
    }
    public Item remove(int index) {
        var i = items[index];
        items[index] = null;
        return i;
    }
    public void swap(int a, int b) {
        var temp = items[a];
        items[a] = items[b];
        items[b] = temp;
    }
    public Optional<Item> find(Predicate<Item> predicate) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (predicate.test(items[i])) {
                return Optional.of(items[i]);
            }
        }
        return Optional.empty();
    }
    public void consume(Item item) {
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (items[i] == item) {
                items[i] = null;
                return;
            }
        }
    }
    public void set(int index, Item item) {
        items[index] = item;
    }
    public void apply(int slot, ItemUseResult result) {
        if (result.leftover().isEmpty()) {
            items[slot] = null;
        } else {
            items[slot] = result.leftover().get();
        }
    }
    public void setEquipment(EquipmentSlot slot, Item item) {
        equipment.put(slot, item);
    }
    public Item getEquipment(EquipmentSlot slot) {
        return equipment.get(slot);
    }
    public int getRoomLeft() {
        int i = 0;
        for (int j = 0; j < NUM_ITEMS; ++j) {
            if (get(j) == null) ++i;
        }
        return i;
    }
}
