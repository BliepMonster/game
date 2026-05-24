package crafting;

public abstract class ItemRequirement {
    public abstract boolean isMet(FullItemStorage storage);
    public abstract void consume(FullItemStorage storage);
    public abstract String getRequirementName();
}
