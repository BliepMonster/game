package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.Game;
import main.RenderContext;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.VelocityChange;
import stats.inventory.items.Item;
import stats.inventory.items.ItemInventory;
import stats.inventory.items.ItemUseResult;
import stats.inventory.resources.InventoryStorage;
import stats.skills.SkillSet;
import tiles.Tile;

import java.awt.*;
import java.util.ArrayList;

import static engine.core.input.Input.isKeyDown;
import static main.Game.TICKS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.*;

public class Player implements Entity {
    private final ArrayList<VelocityChange> velocityChanges = new ArrayList<>();
    private final Vector2f position = new Vector2f();
    private final Hitbox step = new RectangularHitbox(-.5f, 0, 1, .2f, this);
    public StatHolder stats = new StatHolder();
    private int selectedHotbarSlot = 0;
    public ItemInventory items = new ItemInventory();
    public InventoryStorage inventory = new InventoryStorage();
    public SkillSet skills = new SkillSet();
    private int ticksTo = 0;
    private int ticksSinceLastDamage = 0;
    private int ticksSinceLastHeal = 0;
    private int health = 100;

    private StatHolder getBaseStats() {
        return new StatHolder().withSpeed(5).with(StatHolder.Stat.MAX_HEALTH, 100);
    }

    private void updateStats() {
        this.stats = getBaseStats().merge(skills.getStatBoost());
    }

    @Override
    public void tick(GameContext context) {
        if (ticksTo++ == 3) {
            updateStats();
            ticksTo = 0;
        } if (ticksSinceLastHeal++ == Game.TICKS_PER_SECOND) {
            heal((int) stats.get(StatHolder.Stat.MAX_HEALTH)/100); // heal 1% per second
        }
        velocityChanges.removeIf(VelocityChange::canBeRemoved);
        ticksSinceLastDamage++;
    }
    @Override
    public void render(RenderContext context, float x, float y, float tsize) {
        var draw = context.map();
        draw.rect(isDead() ? Color.orange : ticksSinceLastDamage < 5 ? Color.red : Color.blue, x, y, tsize, tsize*2);
    }

    @Override
    public boolean shouldDie(GameContext context) {
        return health == 0;
    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }

    @Override
    public Hitbox getHitbox() {
        return step;
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
            movement.mul(stats.get(StatHolder.Stat.SPEED) / TICKS_PER_SECOND);
        }
        for (VelocityChange change : velocityChanges) {
            movement.add(change.getVelocityChange());
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
    public void selectItem(int i) {
        if (i < 0 || i > 9) throw new UnsupportedOperationException("Invalid slot");
        this.selectedHotbarSlot = i;
    }
    public int getSelectedHotbarSlot() {
        return selectedHotbarSlot;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void damage(int amount, DamageType type, GameContext context) {
        health -= amount;
        if (health <= 0) {
            health = 0;
        }
        ticksSinceLastDamage = 0;
    }
    public void heal(int amount) {
        health += amount;
        if (health > stats.get(StatHolder.Stat.MAX_HEALTH)) {
            health = (int) stats.get(StatHolder.Stat.MAX_HEALTH);
        }
    }
    public boolean isAtFullHealth() {
        return health == (int) stats.get(StatHolder.Stat.MAX_HEALTH);
    }
    public boolean useSelectedItem(GameContext context, Tile tile, Vector2f worldPos) {
        Item item  = this.items.get(selectedHotbarSlot);
        if (item == null) return false;
        ItemUseResult result = item.definition.use(tile, item, context, worldPos);
        this.items.apply(selectedHotbarSlot, result);
        return result.handled();
    }
    private int deathTick = -1000;
    public boolean isDead() {
        return Game.getTickCount() - deathTick < Game.TICKS_PER_SECOND;
    }
    public void respawn(GameContext context) {
        this.health = (int) this.stats.get(StatHolder.Stat.MAX_HEALTH);
        this.position.set(context.getPlayerRespawnPosition());
    }
    public void die(GameContext context) {
        deathTick = Game.getTickCount();
    }
    public boolean isAttackable() {
        return false;
    }
    public void knock(Vector2f direction, float amount) {
        velocityChanges.add(Entity.getVelocityChange(direction, amount, (int) ((float) Game.TICKS_PER_SECOND/10*Math.max(amount/5, 1))));
    }
    public float getWidth() {
        return 1;
    }
    public float getHeight() {
        return .2f;
    }
}
