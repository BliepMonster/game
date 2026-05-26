package stats.inventory;

public class CurrencyStorage {
    private int coins;
    public CurrencyStorage(int coins) {
        this.coins = coins;
    }
    public boolean hasCoins(int coins) {
        return this.coins >= coins;
    }
    public void spend(int coins) {
        if (coins > this.coins) throw new RuntimeException("Spent too much");
        this.coins -= coins;
    }
    public void get(int coins) {
        if (coins < 0) throw new RuntimeException("Use 'spend' instead");
        this.coins += coins;
    }
    public int getCoins() {
        return this.coins;
    }
}
