package tiles.resources;

import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.notifications.NotificationSystem;
import engine.saving.objects.SavedObject;
import main.Camera;
import main.Game;
import main.GameContext;
import main.YieldUtils;
import org.joml.Vector2f;
import particles.ChipBurst;
import stats.inventory.items.ToolItemDefinition;
import tiles.TilePart;

import java.awt.*;

public abstract class HittableResourceCreator extends TilePart implements JobActor {
    private final HittableResourceDescription description;
    private int lastHitTick = -20;
    private boolean wasLastHitValid = true;
    private int health;
    public HittableResourceCreator(HittableResourceDescription description, Vector2f position) {
        super(position);
        this.description = description;
        health = description.health();
    }
    public abstract void renderReady(int lastHitTick, boolean wasLastHitRight, int x, int y, float healthRemaining, Camera camera);
    public abstract void renderUnready(int x, int y, Camera camera);
    public abstract Color[] getChipColors();
    public void render(int x, int y, Camera camera) {
        if (health != 0)
            renderReady(lastHitTick, wasLastHitValid, x, y, health/(float)description.health(), camera);
        else renderUnready(x, y, camera);
    }
    public boolean isStandable() {
        return description.standable();
    }
    public void onStand(GameContext context) {}

    // logic lives in item definition
    public void onClick(GameContext context) {
        wasLastHitValid = false;
        lastHitTick = Game.getTickCount();
    }
    private void grantItems(GameContext context) {
        var player = context.getPlayer();
        int yield = YieldUtils.getYieldEffect(description.yield(), player.stats.get(description.modifier()));
        player.inventory.add(description.itemID(), yield);
        player.skills.giveXP(description.skill(), description.xp());
    }
    public void hit(GameContext context, int power) {
        if (health == 0) return;
        lastHitTick = Game.getTickCount();
        if (context.getPlayer().skills.get(description.skill()).getLevel() < description.levelRequirement()) {
            wasLastHitValid = false;
            NotificationSystem.add("You need "+context.getPlayer().skills.get(description.skill()).getName()+" level "+description.levelRequirement()+"!", Color.RED);
            return;
        }
        if (power == 0) {
            wasLastHitValid = false;
            return;
        }
        health -= power;
        onHit(context);
        if (health <= 0) {
            health = 0;
            spawnJob();
            onBreak(context);
            grantItems(context);
        }
        wasLastHitValid = true;
    }
    public ToolItemDefinition.PowerType getPowerType() {
        return description.powerType();
    }
    public void notify(Job job) {
        health = description.health();
    }
    private void spawnJob() {
        JobSystem.addJob(new ResourceJob(this, (float) description.respawnTicks()/Game.TICKS_PER_SECOND));
    }
    public void onHit(GameContext context) {
        spawnChips(context, 3, .03f, .05f, 6, 3f, 1.5f, 14f);
    }
    protected void onBreak(GameContext context) {
        spawnChips(context, 10, .05f, .09f, 22, 2f, 2f, 12f);
    }
    private void spawnChips(GameContext context, int count, float minSize, float maxSize,
                            int lifeTicks, float speed, float lift, float gravity) {
        Vector2f origin = new Vector2f(position.x + .5f, position.y + .5f);
        Vector2f away = new Vector2f(origin.x - context.getPlayer().getX(), origin.y - context.getPlayer().getY());
        if (away.length() == 0) away.set(0, 1);
        ChipBurst.burst(context, origin, away, count, getChipColors(), minSize, maxSize,
                lifeTicks, speed, .6f, lift, gravity);
    }

    protected boolean isAlive() {
        return this.health > 0;
    }
}
