package stats.inventory.items;

import combat.abilities.ItemAbility;
import main.GameContext;
import main.YieldUtils;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.skills.SkillSet;
import tiles.Tile;
import tiles.resources.MapleTreePart;
import tiles.resources.PineTreePart;

import java.util.Optional;

public class LeafCutterItemAbility extends ItemAbility {
    @Override
    public ItemUseResult use(Tile tile, Item item, Vector2f worldClickLocation, GameContext context) {
        if (tile == null || tile.getPart() == null)
            return new ItemUseResult(Optional.of(item), false);
        if (tile.getPart() instanceof MapleTreePart) {
            var ff = YieldUtils.getYieldEffect(5, context.getPlayer().stats.get(StatHolder.Stat.FORAGING_YIELD));
            context.getPlayer().inventory.add("Maple Leaf", ff);
            context.getPlayer().skills.get(SkillSet.SkillType.FORAGING).getXP(10);
        }
        if (tile.getPart() instanceof PineTreePart) {
            var ff = YieldUtils.getYieldEffect(5, context.getPlayer().stats.get(StatHolder.Stat.FORAGING_YIELD));
            context.getPlayer().inventory.add("Pine Needle", ff);
            context.getPlayer().skills.get(SkillSet.SkillType.FORAGING).getXP(40);
        } else return new ItemUseResult(Optional.of(item), false);
        return new ItemUseResult(Optional.of(item), true);
    }
}
