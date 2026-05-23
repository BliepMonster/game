package stats.inventory.items;

public class Item {
    public final ItemDefinition definition;
    public Item(ItemDefinition definition) {
        this.definition = definition;
    }
    public float getRatioTimeUntilReady() {
        return 1;
    }
}
