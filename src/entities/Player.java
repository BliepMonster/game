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
    private float prevX, prevY;
    private Camera cameraRef;
    private final Hitbox step = new RectangularHitbox(-.35f-1f/16f, -1.01f, .7f+2f/16f, .2f, this);
    public StatHolder stats;
    private int selectedHotbarSlot = 0;
    public final ItemInventory items;
    public final InventoryStorage inventory;
    public final SkillSet skills;
    private int ticksSinceLastDamage = 0;
    private boolean hitLastTick;
    private int lastRespawn = -100;
    private Direction direction = Direction.DOWN;
    private float health = 100;
    private float mana = 100;
    private float overflowHealth = 0;
    private float overflowMana = 0;
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
            if (effect.uid().startsWith(DeployableEntity.EFFECT_ID_PREFIX)) continue;
            effects.add(new StatusEffect(effect));
        }
        this.health = player.health();
        this.mana = player.mana();
        this.items = new ItemInventory(player.items());
    }

    private StatHolder getBaseStats() {
        return new StatHolder().with(StatHolder.Stat.SPEED, 5).with(StatHolder.Stat.MAX_HEALTH, 100)
                .with(StatHolder.Stat.STRENGTH, 100)
                .with(StatHolder.Stat.CRIT_CHANCE, 30)
                .with(StatHolder.Stat.CRIT_DAMAGE, 150)
                .with(StatHolder.Stat.MAGIC_POWER, 100);
    }

    private void updateStats(GameContext context) {
        float oldMaxHealth = stats == null ? health : stats.get(StatHolder.Stat.MAX_HEALTH);
        float oldMaxMana = stats == null ? mana : stats.get(StatHolder.Stat.MAGIC_POWER);
        boolean atMaxHealth = health >= oldMaxHealth;
        boolean atMaxMana = mana >= oldMaxMana;
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
        float newMaxHealth = stats.get(StatHolder.Stat.MAX_HEALTH);
        float newMaxMana = stats.get(StatHolder.Stat.MAGIC_POWER);

        if (health > newMaxHealth) {
            overflowHealth += health - newMaxHealth;
            health = newMaxHealth;
        } else if (overflowHealth > 0) {
            float restore = Math.min(overflowHealth, newMaxHealth - health);
            overflowHealth -= restore;
            health += restore;
        } else if (atMaxHealth) {
            health = newMaxHealth;
        }

        if (mana > newMaxMana) {
            overflowMana += mana - newMaxMana;
            mana = newMaxMana;
        } else if (overflowMana > 0) {
            float restore = Math.min(overflowMana, newMaxMana - mana);
            overflowMana -= restore;
            mana += restore;
        } else if (atMaxMana) {
            mana = newMaxMana;
        }
    }

    @Override
    public void tick(GameContext context) {
        updateStats(context);
        if (ticksSinceLastDamage > 5)
            heal(stats.get(StatHolder.Stat.MAX_HEALTH)/(100*TICKS_PER_SECOND)); // heal 1% per second
        float healing = stats.get(StatHolder.Stat.HEALING);
        if (healing > 0 && health != 0) heal(healing / TICKS_PER_SECOND);
        float maxMana = stats.get(StatHolder.Stat.MAGIC_POWER);
        mana += maxMana * 0.03f / TICKS_PER_SECOND;
        if (mana > maxMana) mana = maxMana;
        velocityChanges.removeIf(VelocityChange::canBeRemoved);
        ticksSinceLastDamage++;
    }
    public static final Texture TEXTURE = new Texture("res/textures/enemies/character.png");
    @Override
    public void render(Camera camera) {
        cameraRef = camera;
        var color = isDead() ? Color.orange : ticksSinceLastDamage < 5 ? Color.red : Color.white;
        camera.addRenderJob(() -> camera.tintedImage(TEXTURE, color, getRenderX() - .5f, getRenderY() - 1, 1, 2), -position.y);
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
    public float getRenderX() {
        if (isDead()) return position.x;
        float alpha = getCameraAlpha();
        return (prevX + (position.x - prevX) * alpha) + .5f;
    }
    @Override
    public float getRenderY() {
        if (isDead()) return position.y;
        float alpha = getCameraAlpha();
        return (prevY + (position.y - prevY) * alpha) + 1;
    }
    private float getCameraAlpha() {
        return cameraRef == null ? 0f : cameraRef.getInterpolationAlpha();
    }

    @Override
    public Hitbox getHitbox() {
        return step;
    }
    private final Vector2f movementScratch = new Vector2f();
    public void move(Hitbox world) {
        prevX = position.x;
        prevY = position.y;
        Vector2f movement = movementScratch;
        movement.set(0, 0);
        if (isKeyDown(GLFW_KEY_LEFT)) {
            movement.x -= 1f;
        }
        if (isKeyDown(GLFW_KEY_RIGHT)) movement.x += 1f;
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
        if (step.collidesWith(world)) position.x -= movement.x;

        movement.x = 0;
        position.y += movement.y;
        if (step.collidesWith(world)) position.y -= movement.y;
    }
    public void selectItem(int i) {
        if (i < 0 || i >= 9) throw new UnsupportedOperationException("Invalid slot");
        this.selectedHotbarSlot = i;
    }
    public int getSelectedHotbarSlot() {
        return selectedHotbarSlot;
    }
    public float getHealth() {
        return health;
    }
    public float getMana() {
        return mana;
    }
    public boolean useMana(float amount) {
        if (mana < amount) return false;
        mana -= amount;
        return true;
    }
    public void setHealth(float health) {
        this.health = health;
    }
    public void damage(int amount, DamageType type, GameContext context) {
        boolean defense = type == DamageType.NORMAL;
        if (Game.getTickCount()-lastRespawn < 80) return;
        float def = stats.get(StatHolder.Stat.DEFENSE);
        if (defense) health -= (amount*(1-def/(def+100)));
        else health -= amount;
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
        this.health = this.stats.get(StatHolder.Stat.MAX_HEALTH);
        this.mana = this.stats.get(StatHolder.Stat.MAGIC_POWER);
        this.overflowHealth = 0;
        this.overflowMana = 0;
        context.getWorld().onPlayerRespawn(context);
        this.lastRespawn = Game.getTickCount();
        this.position.set(context.getPlayerRespawnPosition());
        this.effects.clear();
    }
    public void die(GameContext context) {
        deathTick = Game.getTickCount();
    }
    public boolean isAttackable() {
        return true;
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
        return getFacingPosition(mousePos, new Vector2f());
    }
    public Vector2f getFacingPosition(Vector2f mousePos, Vector2f dest) {
        float x = getX();
        float y = getY()-.8f;
        var dx = mousePos.x - getX();
        var dy = mousePos.y - getY();
        Direction facing;
        if (Math.abs(dx) >= Math.abs(dy)) facing = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        else facing = dy > 0 ? Direction.UP : Direction.DOWN;
        return switch (facing) {
            case UP -> dest.set(x, y+1);
            case DOWN -> dest.set(x, y-1);
            case LEFT -> dest.set(x-1.35f, y);
            case RIGHT -> dest.set(x+1.35f, y);
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
