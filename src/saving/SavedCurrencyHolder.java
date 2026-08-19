package saving;

import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedObject;

public record SavedCurrencyHolder(long coins) {
    public SavedCurrencyHolder(SavedObject object) {
        this((long) (double) object.get());
    }
    public SavedObject save() {
        return new SavedDouble(coins);
    }
}
