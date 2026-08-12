package quests;

import main.GameContext;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;

public class ItemReward extends ObjectiveReward {
    private final String itemId;
    private final int amount;

    public ItemReward(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    @Override
    public void grant(GameContext context) {
        ItemDefinition definition = ItemDefinition.get(itemId);
        for (int i = 0; i < amount; ++i) {
            context.getPlayer().items.add(new Item(definition));
        }
    }

    @Override
    public String getDescription() {
        String display = itemId;
        try {
            display = ItemDefinition.get(itemId).name;
        } catch (RuntimeException ignored) {
        }
        return "+" + amount + " " + display;
    }
}
