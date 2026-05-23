package combat.melee;

import engine.hitboxes.Hitbox;
import entities.DamageType;
import entities.Entity;
import main.Game;
import main.GameContext;
import main.RenderContext;
import org.joml.Vector2f;

public abstract class DisplayEntity implements Entity {
    private final Vector2f position;
    private int spawnTick, duration;
    public DisplayEntity(int spawnTick, int duration, Vector2f position) {
        this.spawnTick = spawnTick;
        this.duration = duration;
        this.position = position;
    }
    public int getSpawnTick() {
        return spawnTick;
    }
    public int getDuration() {
        return duration;
    }
    public void move(Hitbox hitbox) {}
    public void tick(GameContext context) {}
    public abstract void render(RenderContext renderer, float x, float y, float tsize);
    public boolean shouldDie(GameContext context) {
        return Game.getTickCount() - spawnTick >= duration;
    }
    public Hitbox getHitbox() {
        return EmptyHitbox.INSTANCE;
    }
    public Hitbox getWalkHitbox() {
        return EmptyHitbox.INSTANCE;
    }
    public void die(GameContext context) {}
    public void damage(int damage, DamageType type, GameContext context) {}
    public boolean isAttackable() {
        return false;
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
}
