package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.skills.SkillSet;
import tiles.spawner.EnemySpawner;

public class ZombieEnemy extends RespawnableEnemy {
    public static final float AGGRO_RADIUS = 5, DEAGGRO_RADIUS = 12;
    private int health = 100;
    private int lastHitTick = 0, lastHurtTick = 0;
    private static final int HIT_INTERVAL = 10;
    private boolean angry = false;
    public ZombieEnemy(Vector2f position, EnemySpawner spawner) {
        super(position, spawner);
    }
    public void move(Hitbox world) {
        var context = getContext();
        var x = context.getPlayer().getX();
        var y = context.getPlayer().getY();
        var diff = new Vector2f(x - position.x, y - position.y);
        var move = angry ? diff.normalize(new Vector2f()).mul(2f / Game.TICKS_PER_SECOND) : new Vector2f();
        if (diff.length() < AGGRO_RADIUS) angry = true;
        else if (Game.getTickCount() - lastHurtTick < Game.TICKS_PER_SECOND*5) angry = true;
        else if (diff.length() >= DEAGGRO_RADIUS) angry = false;
        move.add(getVelocityChange());
        position.x += move.x;
        if (getHitbox().collidesWith(world)) position.x -= move.x;
        position.y += move.y;
        if (getHitbox().collidesWith(world)) position.y -= move.y;
    }
    public float getKnockbackResistance() {
        return 1;
    }
    public void tickSelf(GameContext context) {
        if (context.getPlayer().getHitbox().collidesWith(getHitbox())) {
            if (Game.getTickCount() - lastHitTick >= HIT_INTERVAL) {
                context.getPlayer().damage(20, DamageType.NORMAL, context);
                context.getPlayer().knock(new Vector2f(context.getPlayer().getX() - position.x, context.getPlayer().getY() - position.y).normalize(), 1);
                lastHitTick = Game.getTickCount();
            }
        }
    }
    public void render(Camera camera) {
        var color = Game.getTickCount() <= lastHurtTick+5 ? java.awt.Color.red : java.awt.Color.orange;
        camera.addRenderJob(() -> camera.rect(color, position.x, position.y, 1, 1), -position.y);
    }
    public boolean shouldDie(GameContext context) {
        return health <= 0;
    }
    private final Hitbox hitbox = new RectangularHitbox(0, 0, 1, 1, this);
    public Hitbox getHitbox() {
        return hitbox;
    }
    public void die(GameContext context) {
        super.die(context);
        context.getPlayer().skills.giveXP(SkillSet.SkillType.COMBAT, 10);
    }
    public void damage(int damage, DamageType type, GameContext context) {
        health -= damage;
        angry = true;
        lastHurtTick = Game.getTickCount();
    }
    public float getWidth() {
        return 1;
    }
    public float getHeight() {
        return 1;
    }
}
