package entities.dungeons;

import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.LootTable;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

public abstract class MeleeDungeonEnemy extends DungeonEnemy {
    private boolean angry = false;
    private final float aggroRadius, deaggroRadius;
    private final float defense;
    private final int damage;
    private final int hitInterval;
    private final float speed;

    public MeleeDungeonEnemy(Vector2f position, float hp, int xp, LootTable loot, float aggroRadius, float deaggroRadius, float defense, int damage, int hitInterval, float speed) {
        super(position, hp, xp, loot);
        this.aggroRadius = aggroRadius;
        this.deaggroRadius = deaggroRadius;
        this.speed = speed;
        this.defense = defense;
        this.damage = damage;
        this.hitInterval = hitInterval;
    }

    private int lastHitTick = 0;
    public void tickSelf(GameContext context) {
        if (context.getPlayer().getHitbox().collidesWith(getHitbox())) {
            if (Game.getTickCount() - lastHitTick >= hitInterval) {
                context.getPlayer().damage(damage, DamageType.NORMAL, context);
                context.getPlayer().knock(new Vector2f(context.getPlayer().getX() - position.x, context.getPlayer().getY() - position.y).normalize(), 2);
                lastHitTick = Game.getTickCount();
            }
        }
    }
    public void moveSelf(Hitbox world) {
        var context = getContext();
        var x = context.getPlayer().getX();
        var y = context.getPlayer().getY();
        var diff = new Vector2f(x - position.x, y - position.y);
        var move = angry ? diff.normalize(new Vector2f()).mul(speed / Game.TICKS_PER_SECOND) : new Vector2f();
        if (diff.length() < aggroRadius) angry = true;
        else if (Game.getTickCount() - lastHurtTick < Game.TICKS_PER_SECOND*5) angry = true;
        else if (diff.length() >= deaggroRadius) angry = false;
        position.x += move.x;
        if (getHitbox().collidesWith(world)) position.x -= move.x;
        position.y += move.y;
        if (getHitbox().collidesWith(world)) position.y -= move.y;
    }

    @Override
    public float transformHealthLoss(float hpLoss, DamageType type) {
        if (type == DamageType.NORMAL)
            return hpLoss*(1-defense/(defense+100));
        return hpLoss;
    }

    @Override
    public float getKnockbackResistance() {
        return Math.max((float) Math.log(defense)/100, 1);
    }
}
