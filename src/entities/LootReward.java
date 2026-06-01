package entities;

import crafting.FullItemStorage;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;

import java.util.HashMap;

public class LootReward {
    public final int coins;
    public final ItemDefinition[] items;
    public final HashMap<String, Integer> loot;
    public LootReward(int coins, ItemDefinition[] items, HashMap<String, Integer> loot) {
        this.coins = coins;
        this.items = items;
        this.loot = loot;
    }
    public void apply(FullItemStorage storage) {
        for (var definition : items) {
            storage.inventory.add(new Item(definition));
        }
        storage.currency.get(coins);
        for (var entry : loot.entrySet()) {
            storage.storage.add(entry.getKey(), entry.getValue());
        }
    }
}
