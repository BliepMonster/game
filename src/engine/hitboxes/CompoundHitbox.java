package engine.hitboxes;

import java.io.Serializable;
import java.util.*;

public class CompoundHitbox implements Hitbox, Serializable {
    protected List<Hitbox> parts;
    public CompoundHitbox(List<Hitbox> hitboxes) {
        parts = hitboxes;
    } public CompoundHitbox() {
        parts = new ArrayList<>();
    }
    public CompoundHitbox(Hitbox... hitboxes) {
        parts = new ArrayList<>(Arrays.asList(hitboxes));
    }
    public boolean collidesWith(Hitbox hitbox) {
        boolean compound = hitbox instanceof CompoundHitbox;
        for (Hitbox thisPart : parts) {
            if (compound) {
                for (Hitbox part : ((CompoundHitbox) hitbox).parts) {
                    if (thisPart.collidesWith(part)) {
                        return true;
                    }
                }
            }
        }
        return false;
    } public void add(Hitbox r) {
        parts.add(r);
    }
}
