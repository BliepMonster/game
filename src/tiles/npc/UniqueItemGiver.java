package tiles.npc;

import crafting.FullItemStorage;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;

public class UniqueItemGiver implements ItemGiver {
    private final ItemDefinition definition;
    public UniqueItemGiver(ItemDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean canGrant(FullItemStorage storage) {
        return !storage.inventory.isFull();
    }

    @Override
    public void grant(FullItemStorage storage) {
        storage.inventory.add(new Item(definition));
    }

    @Override
    public String getName() {
        return definition.name;
    }

    @Override
    public String getId() {
        return definition.id;
    }
}
