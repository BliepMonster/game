package quests;

import main.GameContext;

public class CurrencyObjective extends Objective {
    private final int coins;
    private final String name;

    public CurrencyObjective(int coins, String name) {
        this.coins = coins;
        this.name = name;
    }

    @Override
    public boolean isDone(GameContext context) {
        return context.getPlayer().currency.hasCoins(coins);
    }

    @Override
    public int getProgress(GameContext context) {
        return Math.min(context.getPlayer().currency.getCoins(), coins);
    }

    @Override
    public int getTotalTasks(GameContext context) {
        return coins;
    }

    @Override
    public String getType() {
        return "currency";
    }

    @Override
    public String getName() {
        if (name != null) return name;
        return "Accumulate " + coins + " coins";
    }
}
