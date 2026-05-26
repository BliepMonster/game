package tiles.npc;

import crafting.FullItemStorage;

public class ResourceGiver implements ItemGiver {
    private final String id;
    private final int amount;
    public ResourceGiver(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }
    public boolean canGrant(FullItemStorage storage) {
        return true;
    }
    public void grant(FullItemStorage storage) {
        storage.storage.add(id, amount);
    }
    public String getName() {
        return id;
    }
}
