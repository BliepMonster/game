package quests;

import main.GameContext;

public class CurrencyReward extends ObjectiveReward {
    private final int coins;

    public CurrencyReward(int coins) {
        this.coins = coins;
    }

    @Override
    public void grant(GameContext context) {
        context.getPlayer().currency.get(coins);
    }

    @Override
    public String getDescription() {
        return "+" + coins + " coins";
    }
}
