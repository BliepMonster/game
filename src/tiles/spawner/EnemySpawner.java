package tiles.spawner;

import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import main.*;
import tiles.TilePart;
import tiles.resources.ResourceJob;

public abstract class EnemySpawner extends TilePart implements JobActor {
    protected final GameContext context;
    protected final String worldToSpawn;
    protected final int ticksToRespawn;
    public EnemySpawner(GameContext context, int ticksToRespawn, String worldID) {
        this.context = context;
        this.ticksToRespawn = ticksToRespawn;
        this.worldToSpawn = worldID;
        applyRespawnJob();
    }
    public void onMobDeath() {
        applyRespawnJob();
    }
    public void render(int x, int y, Camera camera) {}
    public boolean isStandable() {
        return true;
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {}
    private void applyRespawnJob() {
        JobSystem.addJob(new ResourceJob(this, ticksToRespawn/(float) Game.TICKS_PER_SECOND));
    }
    public World getWorld() {
        return WorldRegistry.get(worldToSpawn);
    }
}
