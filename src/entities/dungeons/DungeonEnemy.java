package entities.dungeons;

import crafting.FullItemStorage;
import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.Enemy;
import entities.LootTable;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.skills.SkillSet;

public abstract class DungeonEnemy extends Enemy {
    private float hp;
    private final int xp;
    private final LootTable loot;
    protected int lastHurtTick = 0;
    public DungeonEnemy(Vector2f position, float hp, int xp, LootTable loot) {
        super(position);
        this.hp = hp;
        this.xp = xp;
        this.loot = loot;
    }
    public abstract void moveSelf(Hitbox world);
    public void move(Hitbox world) {
        moveSelf(world);
        var vec = getVelocityChange();
        position.add(vec);
        if (this.getHitbox().collidesWith(world)) position.sub(vec);
    }
    public abstract float transformHealthLoss(float hpLoss, DamageType type);
    public boolean shouldDie(GameContext context) {
        return hp < 0;
    }
    public void die(GameContext context) {
        var player = context.getPlayer();
        player.skills.giveXP(SkillSet.SkillType.COMBAT, xp);
        loot.apply(new FullItemStorage(
                player.items,
                player.inventory,
                player.currency
        ));
    }

    @Override
    public void damage(int damage, DamageType type, GameContext context) {
        this.hp -= transformHealthLoss(damage, type);
        lastHurtTick = Game.getTickCount();
    }
}
