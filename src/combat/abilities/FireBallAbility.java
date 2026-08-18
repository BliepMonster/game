package combat.abilities;

import entities.projectiles.FireballProjectile;
import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;

import java.util.Optional;

public class FireBallAbility extends ItemAbility {
    @Override
    public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
        context.spawn(new FireballProjectile(context.getPlayer().getPosition(), worldClickLocation.sub(context.getPlayer().getPosition(), new Vector2f()), 80, context.getPlayer()));
        return new ItemUseResult(Optional.of(item), true);
    }
}
