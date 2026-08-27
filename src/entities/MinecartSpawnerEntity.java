package entities;

import combat.melee.EmptyHitbox;
import engine.hitboxes.Hitbox;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

public class MinecartSpawnerEntity implements Entity {
    public static final int COOLDOWN = 80;
    public static final float CHANCE = 0.001f;
    int last = Game.getTickCount();

    @Override
    public void die(GameContext context) {

    }

    @Override
    public void knock(Vector2f direction, float amount) {

    }

    @Override
    public void damage(int damage, DamageType type, GameContext context) {

    }

    @Override
    public void move(Hitbox world) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }
    public MinecartSpawnerEntity(Vector2f ignored) {

    }

    @Override
    public void tick(GameContext context) {
        if (Game.getTickCount()-this.last > COOLDOWN && Math.random() < CHANCE) {
            context.spawn(new MinecartEntity(new Vector2f(6, 12)));
            this.last = Game.getTickCount();
        }
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Hitbox getHitbox() {
        return EmptyHitbox.INSTANCE;
    }

    @Override
    public void render(Camera camera) {

    }

    @Override
    public boolean shouldDie(GameContext context) {
        return false;
    }
}
