package crafting;

public class ResourceRequirement extends ItemRequirement {
    public final String resourceName;
    public final int amount;
    public ResourceRequirement(String resourceName, int amount) {
        this.resourceName = resourceName;
        this.amount = amount;
    }
    public String getRequirementName() {
        return amount+" "+resourceName;
    }

    @Override
    public boolean isMet(FullItemStorage storage) {
        return storage.storage.contains(resourceName, amount);
    }
    public void consume(FullItemStorage storage) {
        storage.storage.spend(resourceName, amount);
    }
}
