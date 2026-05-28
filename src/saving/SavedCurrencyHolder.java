package saving;

import engine.saving.objects.SavedFloat;
import engine.saving.objects.SavedObject;

public record SavedCurrencyHolder(int coins) {
    public SavedCurrencyHolder(SavedObject object) {
        this((int) (float) object.get());
    }
    public SavedObject save() {
        return new SavedFloat(coins);
    }
}
