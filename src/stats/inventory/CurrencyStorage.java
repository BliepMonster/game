package stats.inventory;

import saving.SavedCurrencyHolder;
import tiles.npc.Currency;

public class CurrencyStorage {
    private long coins;
    public CurrencyStorage(long coins) {
        this.coins = coins;
    }
    public CurrencyStorage(SavedCurrencyHolder holder) {
        this.coins = holder.coins();
    }
    public boolean hasCoins(long coins) {
        return this.coins >= coins;
    }
    public void spend(long coins) {
        if (coins > this.coins) throw new RuntimeException("Spent too much");
        this.coins -= coins;
    }
    public void get(long coins) {
        if (coins < 0) throw new RuntimeException("Use 'spend' instead");
        this.coins += coins;
    }
    public long getCoins() {
        return this.coins;
    }
    public long get(Currency currency) {
        return switch (currency) {
            case COINS -> coins;
        };
    }
}
