package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.RenderContext;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.InventoryStorage;
import stats.skills.SkillSet;

import java.awt.*;

import static engine.core.input.Input.isKeyDown;
import static main.Game.TICKS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.*;

public class Player implements Entity {
    private final Vector2f position = new Vector2f();
    private final Hitbox step = new RectangularHitbox(-.5f, 0, 1, .2f, this);
    private final Hitbox hit = new RectangularHitbox(-.5f, 0, 1, 2, this);
    public StatHolder stats = new StatHolder();
    public InventoryStorage inventory = new InventoryStorage();
    public SkillSet skills = new SkillSet();
    private int ticksTo = 0;

    private StatHolder getBaseStats() {
        return new StatHolder().withSpeed(5);
    }

    private void updateStats() {
        this.stats = getBaseStats().merge(skills.getStatBoost());
        System.out.println(stats.foragingYield);
    }

    @Override
    public void tick(GameContext context) {
        if (ticksTo++ == 3) {
            updateStats();
            ticksTo = 0;
        }
    }
    @Override
    public void render(RenderContext context, float x, float y, float tsize) {
        var draw = context.map();
        draw.rect(Color.blue, x, y, tsize, tsize*2);
    }

    @Override
    public boolean shouldDie(GameContext context) {
        return false;
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }

    @Override
    public Hitbox getHitbox() {
        return hit;
    }

    @Override
    public Hitbox getWalkHitbox() {
        return step;
    }
    public void move(Hitbox world) {
        var movement = new Vector2f(0, 0);
        if (isKeyDown(GLFW_KEY_LEFT)) {
            movement.x -= 1f;
        }
        if (isKeyDown(GLFW_KEY_RIGHT)) movement.x += 1f;
        var m = false;
        if (isKeyDown(GLFW_KEY_UP)) {
            movement.y += 1f;
        }
        if (isKeyDown(GLFW_KEY_DOWN)) {
            movement.y -= 1f;
        }
        if (movement.x != 0 || movement.y != 0) {
            movement.normalize();
            movement.mul(stats.speed / TICKS_PER_SECOND);
        }
        position.x += movement.x;
        if (movement.x != 0)
            m = true;
        if (step.collidesWith(world)) position.x -= movement.x;

        movement.x = 0;
        position.y += movement.y;
        if (movement.y != 0)
            m = true;
        if (step.collidesWith(world)) position.y -= movement.y;

        if (!m) {
            position.x = Math.round(position.x * 16) / 16f;
            position.y = Math.round(position.y * 16) / 16f;
        }
    }
}
