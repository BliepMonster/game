package combat.abilities;

import entities.projectiles.FireballProjectile;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

import java.util.Optional;

public class FireBallAbility extends ItemAbility {
    private static final float MANA_COST = 35;
    @Override
    public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
        var player = context.getPlayer();
        if (!player.useMana(MANA_COST)) return new ItemUseResult(Optional.of(item), false);
        float magicPower = player.stats.get(StatHolder.Stat.MAGIC_POWER);
        int damage = (int) (80 * magicPower / 100);
        context.spawn(new FireballProjectile(player.getPosition().add(0, 0, new Vector2f()), worldClickLocation.sub(player.getPosition(), new Vector2f()).normalize(), damage, player));
        return new ItemUseResult(Optional.of(item), true);
    }
}
