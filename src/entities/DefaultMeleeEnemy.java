package entities;

import main.Camera;
import org.joml.Vector2f;

public abstract class DefaultMeleeEnemy extends MeleeEnemy {
    public float getKnockbackResistance() {
        return 1;
    }

    public DefaultMeleeEnemy(Vector2f vec, float hp, float aggro, float deaggro, int xpGain, int damage, float w, float h, int cooldown) {
        super(vec, hp, aggro, deaggro, xpGain, damage, w, h, cooldown);
    }

    public abstract void render(Camera camera);
}
