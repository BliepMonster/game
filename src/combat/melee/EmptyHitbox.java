package combat.melee;

import engine.hitboxes.Hitbox;

public class EmptyHitbox implements Hitbox {
    public static final EmptyHitbox INSTANCE = new EmptyHitbox();
    private EmptyHitbox() {}
    public boolean collidesWith(Hitbox hitbox) {
        return false;
    }
}
