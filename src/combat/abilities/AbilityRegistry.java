package combat.abilities;

import alchemy.AlchemyStationTilePart;
import entities.projectiles.FireballProjectile;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.StatusEffect;
import stats.inventory.items.Item;
import stats.inventory.items.ItemUseResult;
import tiles.Tile;
import tiles.TilePart;
import tiles.crafting.CookingStationTilePart;

import java.util.HashMap;
import java.util.Optional;

public class AbilityRegistry {
    public static final HashMap<String, ItemAbility> registry = new HashMap<>();
    static {
        registry.put("copper_hoe", new HoeHarvestAbility(2));
        registry.put("iron_hoe", new HoeHarvestAbility(3));
        registry.put("steel_hoe", new HoeHarvestAbility(5));
        registry.put("crafting_station", new CraftingTablePlacerAbility());
        registry.put("deconstructor", new DeconstructorPlacerAbility());
        registry.put("chest", new ChestPlacerAbility());
        registry.put("furnace", new FurnacePlacerAbility());
        registry.put("crusher", new CrusherPlacerAbility());
        registry.put("scroll_table", new ScrollTablePlacerAbility());
        registry.put("polisher", new PolisherPlacerAbility());
        registry.put("garden_pot", new GardenPotPlacerAbility());
        registry.put("seed_maker", new SeedMakerPlacerAbility());
        registry.put("wardrobe", new WardrobePlacerAbility());
        registry.put("anvil", new AnvilPlacerAbility());
        registry.put("fireball", new FireBallAbility());
        registry.put("alchemy_station", new PlacerAbility() {
            @Override
            public TilePart getPart(Vector2f position) {
                return new AlchemyStationTilePart(position);
            }
        });
        registry.put("cooking_station", new PlacerAbility() {
            @Override
            public TilePart getPart(Vector2f position) {
                return new CookingStationTilePart(position);
            }
        });
        registry.put("healing_1", new ItemAbility() {
            @Override
            public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
                var player = context.getPlayer();
                if (!player.useMana(20)) return new ItemUseResult(Optional.of(item), false);
                player.effects.add(new StatusEffect("healing_1", new StatHolder().with(StatHolder.Stat.HEALING, 20), 100));
                return new ItemUseResult(Optional.of(item), true);
            }
        });
    }
}
