package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.skills.SkillSet;

public abstract class MeleeEnemy extends Enemy {
    private float health;
    private final float aggroRadius, deaggroRadius;
    private int lastHurtTick = 0;
    private boolean angry = false;
    private final int xp;
    private final int damage;
    private final float w, h;
    private final int cooldown;
    private int lastHit = 0;
    private final Vector2f diff = new Vector2f(), move = new Vector2f();
    public MeleeEnemy(Vector2f vec, float hp, float aggro, float deaggro, int xpGain, int damage, float w, float h, int cooldown) {
        super(vec);
        this.xp = xpGain;
        this.health = hp;
        this.w = w;
        this.h = h;
        this.cooldown = cooldown;
        this.aggroRadius = aggro;
        this.deaggroRadius = deaggro;
        this.damage = damage;
        hitbox = new RectangularHitbox(0, 0, w, h, this);
    }
    public abstract float getKnockbackResistance();
    public void tickSelf(GameContext context) {
        if (context.getPlayer().getHitbox().collidesWith(getHitbox()) && Game.getTickCount()-lastHit > cooldown) {
            context.getPlayer().damage(damage, DamageType.NORMAL, context);
            lastHit = Game.getTickCount();
            context.getPlayer().knock(new Vector2f(context.getPlayer().getX() - position.x, context.getPlayer().getY() - position.y).normalize(), 1);
        }
    }
    public void move(Hitbox world) {
        var context = getContext();
        var x = context.getPlayer().getX();
        var y = context.getPlayer().getY();
        diff.set(x - position.x, y - position.y);
        if (angry) move.set(diff).normalize().mul(2f / Game.TICKS_PER_SECOND); else move.set(0, 0);
        if (diff.length() < aggroRadius) angry = true;
        else if (Game.getTickCount() - lastHurtTick < Game.TICKS_PER_SECOND*5) angry = true;
        else if (diff.length() >= deaggroRadius) angry = false;
        move.add(getVelocityChange());
        position.x += move.x;
        if (getHitbox().collidesWith(world)) position.x -= move.x;
        position.y += move.y;
        if (getHitbox().collidesWith(world)) position.y -= move.y;
    }
    public abstract void render(Camera camera);
    public int getLastHurtTick() {
        return lastHurtTick;
    }
    public boolean shouldDie(GameContext context) {
        return health <= 0;
    }

    public abstract void giveLoot(GameContext context);

    @Override
    public void die(GameContext context) {
        context.getPlayer().skills.giveXP(SkillSet.SkillType.COMBAT, xp);
        giveLoot(context);
    }
    public void damage(int damage, DamageType type, GameContext context) {
        health -= damage;
        angry = true;
        lastHurtTick = Game.getTickCount();
    }

    private final Hitbox hitbox;
    public Hitbox getHitbox() {
        return hitbox;
    }
    public float getWidth() {
        return w;
    }
    public float getHeight() {
        return h;
    }
}
