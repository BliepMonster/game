package entities;

import combat.equipment.EquipmentSlot;
import engine.core.Texture;
import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import main.LightSource;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import saving.SavedPlayer;
import stats.StatHolder;
import stats.StatusEffect;
import stats.VelocityChange;
import stats.inventory.CurrencyStorage;
import stats.inventory.items.Item;
import stats.inventory.items.ItemInventory;
import stats.inventory.items.ItemUseResult;
import stats.inventory.resources.InventoryStorage;
import stats.skills.SkillSet;
import tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import static engine.core.input.Input.isKeyDown;
import static main.Game.TICKS_PER_SECOND;
import static org.lwjgl.glfw.GLFW.*;

public class Player implements Entity, LightSource {
    private final ArrayList<VelocityChange> velocityChanges = new ArrayList<>();
    private final Vector2f position = new Vector2f();
    private final Hitbox step = new RectangularHitbox(-.425f, -1f, .85f, .2f, this);
    public StatHolder stats;
    private int selectedHotbarSlot = 0;
    public final ItemInventory items;
    public final InventoryStorage inventory;
    public final SkillSet skills;
    private int ticksTo = 0;
    private int ticksSinceLastDamage = 0;
    private boolean hitLastTick;
    private int lastRespawn = -100;
    private Direction direction = Direction.DOWN;
    private float health = 100;
    public final CurrencyStorage currency;
    public final HashSet<StatusEffect> effects = new HashSet<>();
    public Player() {
        currency = new CurrencyStorage(0);
        skills = new SkillSet();
        inventory = new InventoryStorage();
        items = new ItemInventory();
    }
    public Player(SavedPlayer player) {
        this.currency = new CurrencyStorage(player.currency());
        this.skills = new SkillSet(player.skills());
        this.inventory = new InventoryStorage(player.storage());
        for (var effect : player.statusEffects()) {
            effects.add(new StatusEffect(effect));
        }
        this.health = player.health();
        this.items = new ItemInventory(player.items());
    }

    private StatHolder getBaseStats() {
        return new StatHolder().withSpeed(5).with(StatHolder.Stat.MAX_HEALTH, 100)
                .with(StatHolder.Stat.STRENGTH, 100)
                .with(StatHolder.Stat.CRIT_CHANCE, 30)
                .with(StatHolder.Stat.CRIT_DAMAGE, 150);
    }

    private void updateStats(GameContext context) {
        var i = items.get(selectedHotbarSlot);
        var helmet = items.getEquipment(EquipmentSlot.HELMET);
        var cp = items.getEquipment(EquipmentSlot.CHESTPLATE);
        var ring = items.getEquipment(EquipmentSlot.RING);
        var legs = items.getEquipment(EquipmentSlot.LEGGINGS);
        var boots = items.getEquipment(EquipmentSlot.BOOTS);
        this.stats = getBaseStats().merge(skills.getStatBoost());
        if (i != null) stats.merge(i.definition.getHeldStats_(i, context));
        if (helmet != null) stats.merge(helmet.definition.getWornStats_(helmet, context));
        if (cp != null) stats.merge(cp.definition.getWornStats_(cp, context));
        if (ring != null) stats.merge(ring.definition.getWornStats_(ring, context));
        if (legs != null) stats.merge(legs.definition.getWornStats_(legs, context));
        if (boots != null) stats.merge(boots.definition.getWornStats_(boots, context));
        effects.removeIf(StatusEffect::isDone);
        for (var effect : effects) {
            stats.merge(effect.stats);
        }
    }

    @Override
    public void tick(GameContext context) {
        if (ticksTo++ == 3 || stats == null) {
            updateStats(context);
            ticksTo = 0;
        }
        if (ticksSinceLastDamage > 5)
            heal(stats.get(StatHolder.Stat.MAX_HEALTH)/(100*TICKS_PER_SECOND)); // heal 1% per second
        velocityChanges.removeIf(VelocityChange::canBeRemoved);
        ticksSinceLastDamage++;
    }
    public static final Texture TEXTURE = new Texture("res/textures/enemies/character.png");
    @Override
    public void render(Camera camera) {
        var color = isDead() ? Color.orange : ticksSinceLastDamage < 5 ? Color.red : Color.white;
        camera.addRenderJob(() -> camera.tintedImage(TEXTURE, color, position.x, position.y, 1, 2), -position.y);
    }

    @Override
    public boolean shouldDie(GameContext context) {
        return health == 0;
    }
    public float getX() {
        return position.x+.5f;
    }
    public float getY() {
        return position.y+1;
    }

    @Override
    public Hitbox getHitbox() {
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
            if (movement.x != 0)
                direction = movement.x > 0 ? Direction.RIGHT : Direction.LEFT;
            else direction = movement.y > 0 ? Direction.UP : Direction.DOWN;
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
    public float getHealth() {
        return health;
    }
    public void setHealth(float health) {
        this.health = health;
    }
    public void damage(int amount, DamageType type, GameContext context) {
        if (Game.getTickCount()-lastRespawn < 80) return;
        float def = stats.get(StatHolder.Stat.DEFENSE);
        health -= (amount*(1-def/(def+100)));
        if (health <= 0) {
            health = 0;
        }
        ticksSinceLastDamage = 0;
    }
    public void heal(float amount) {
        health += amount*(stats.get(StatHolder.Stat.REGENERATION)/100+1);
        if (health > stats.get(StatHolder.Stat.MAX_HEALTH)) {
            health = stats.get(StatHolder.Stat.MAX_HEALTH);
        }
    }
    public boolean isAtFullHealth() {
        return health == stats.get(StatHolder.Stat.MAX_HEALTH);
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
        context.getWorld().onPlayerRespawn(context);
        this.lastRespawn = Game.getTickCount();
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
    public void addEffect(StatusEffect effect) {
        effects.add(effect);
    }
    public Vector2f getPosition() {
        return position;
    }
    public Direction getDirection() {
        return direction;
    }
    public Vector2f getFacingPosition(Vector2f mousePos) {
        float x = getX();
        float y = getY()-1;
        var dx = mousePos.x - getX();
        var dy = mousePos.y - getY();
        Direction facing;
        if (Math.abs(dx) >= Math.abs(dy)) facing = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        else facing = dy > 0 ? Direction.UP : Direction.DOWN;
        return switch (facing) {
            case UP -> new Vector2f(x, y+1);
            case DOWN -> new Vector2f(x, y-1);
            case LEFT -> new Vector2f(x-1.425f, y);
            case RIGHT -> new Vector2f(x+1.425f, y);
        };
    }

    @Override
    public float getLightLevel() {
        if (stats == null) return 0;
        return stats.get(StatHolder.Stat.GLOWING);
    }

    @Override
    public float getAttractionX() {
        return position.x;
    }
    public float getAttractionY() {
        return position.y;
    }
}
