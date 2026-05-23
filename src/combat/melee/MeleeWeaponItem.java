package combat.melee;

import main.Game;
import stats.inventory.items.Item;

public class MeleeWeaponItem extends Item {
    private int lastAttack;
    public MeleeWeaponItem(MeleeWeaponItemDefinition definition) {
        super(definition);
        this.lastAttack = Game.getTickCount();
    }
    public void setLastAttack(int l) {
        this.lastAttack = l;
    }
    public int getLastAttack() {
        return lastAttack;
    }
    @Override
    public float getRatioTimeUntilReady() {
        return Math.min(((float) Game.getTickCount()-lastAttack)/((MeleeWeaponItemDefinition) definition).cooldownTicks, 1);
    }
}
