package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Game;
import main.GameContext;
import main.RenderContext;
import org.joml.Vector2f;
import stats.VelocityChange;

import java.util.ArrayList;

public class TestEnemy implements Entity {
    private Vector2f position;
    private final ArrayList<VelocityChange> velocityChanges = new ArrayList<>();
    private int health = 100;
    private int lastHitTick = 0, lastHurtTick = 0;
    private static final int HIT_INTERVAL = 10;
    private GameContext context;
    public TestEnemy(Vector2f position) {
        this.position = position;
    }
    public void move(Hitbox world) {
        var x = context.getPlayer().getX();
        var y = context.getPlayer().getY();
        var move = new Vector2f(x - position.x, y - position.y).normalize().mul(2f/Game.TICKS_PER_SECOND);
        for (var change : velocityChanges) {
            move.add(change.getVelocityChange());
        }
        position.x += move.x;
        if (getHitbox().collidesWith(world)) position.x -= move.x;
        position.y += move.y;
        if (getHitbox().collidesWith(world)) position.y -= move.y;
    }
    public void tick(GameContext context) {
        this.context = context;
        if (context.getPlayer().getHitbox().collidesWith(getHitbox())) {
            if (Game.getTickCount() - lastHitTick >= HIT_INTERVAL) {
                context.getPlayer().damage(5, DamageType.NORMAL, context);
                context.getPlayer().knock(new Vector2f(context.getPlayer().getX() - position.x, context.getPlayer().getY() - position.y).normalize(), 1);
                lastHitTick = Game.getTickCount();
            }
        }
        this.velocityChanges.removeIf(VelocityChange::canBeRemoved);
    }
    public void render(RenderContext context, float x, float y, float tsize) {
        context.map().rect(Game.getTickCount() <= lastHurtTick+5 ? java.awt.Color.red : java.awt.Color.orange, x, y, tsize, tsize);
    }
    public boolean shouldDie(GameContext context) {
        return health <= 0;
    }
    private final Hitbox hitbox = new RectangularHitbox(0, 0, 1, 1, this);
    public Hitbox getHitbox() {
        return hitbox;
    }
    public Hitbox getWalkHitbox() {
        return hitbox;
    }
    public void die(GameContext context) {}
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public void damage(int damage, DamageType type, GameContext context) {
        health -= damage;
        lastHurtTick = Game.getTickCount();
    }
    public boolean isAttackable() {
        return true;
    }
    public void knock(Vector2f direction, float amount) {
        velocityChanges.add(Entity.getVelocityChange(direction, amount, (int) ((float) Game.TICKS_PER_SECOND /10*Math.max(amount/5, 1))));
    }
    public float getWidth() {
        return 1;
    }
    public float getHeight() {
        return 1;
    }
}
