package tiles.npc;

import crafting.FullItemStorage;

public class MoneyRequirement {
    public final Currency currency;
    public final int price;
    public MoneyRequirement(Currency currency, int price) {
        this.currency = currency;
        this.price = price;
    }
    public boolean hasEnough(FullItemStorage storage) {
        return switch (currency) {
            case COINS -> storage.currency.hasCoins(price);
        };
    }
    public void spend(FullItemStorage storage) {
        switch (currency) {
            case COINS -> storage.currency.spend(price);
        }
    }
}
