package tiles.npc;

import crafting.FullItemStorage;

public interface ItemGiver {
    boolean canGrant(FullItemStorage storage);
    void grant(FullItemStorage storage);
    String getName();
    String getId();
}
