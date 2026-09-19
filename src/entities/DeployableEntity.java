package entities;

import combat.melee.EmptyHitbox;
import engine.hitboxes.Hitbox;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import stats.StatusEffect;
import stats.inventory.items.DeployableItemDefinition;

import java.util.concurrent.atomic.AtomicInteger;

public class DeployableEntity implements Entity {
    private static final AtomicInteger NEXT_EFFECT_ID = new AtomicInteger();
    public static final String EFFECT_ID_PREFIX = "deployable_effect:";
    public final Vector2f position;
    public final DeployableItemDefinition definition;
    public final int spawned = Game.getTickCount();
    private final String effectId;
    private boolean applying;
    private Player playerRef;

    @Override
    public void move(Hitbox world) {

    }

    @Override
    public void tick(GameContext context) {
        var player = context.getPlayer();
        this.playerRef = player;
        boolean inside = player.getPosition().sub(this.position, new Vector2f()).length() <= definition.radius;
        if (inside) {
            player.effects.removeIf(effect -> effectId.equals(effect.id));
            player.effects.add(new StatusEffect(effectId, definition.buffs, definition.duration));
            applying = true;
        } else if (applying) {
            player.effects.removeIf(effect -> effectId.equals(effect.id));
            applying = false;
        }
    }

    @Override
    public void onUnload() {
        if (playerRef != null) playerRef.effects.removeIf(effect -> effectId.equals(effect.id));
        applying = false;
    }

    @Override
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.image(definition.deployableTexture, position.x-.5f, position.y-.5f, 1, 1), -position.y+.5f);
    }

    @Override
    public boolean shouldDie(GameContext context) {
        return Game.getTickCount()-this.spawned > definition.duration;
    }

    @Override
    public Hitbox getHitbox() {
        return EmptyHitbox.INSTANCE;
    }

    @Override
    public void die(GameContext context) {
        context.getPlayer().effects.removeIf(effect -> effectId.equals(effect.id));
        applying = false;
    }

    @Override
    public void damage(int damage, DamageType type, GameContext context) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void knock(Vector2f direction, float amount) {

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
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    public DeployableEntity(Vector2f position, DeployableItemDefinition definition) {
        this.position = position;
        this.definition = definition;
        this.effectId = EFFECT_ID_PREFIX + definition.id + "_" + NEXT_EFFECT_ID.incrementAndGet();
    }
}