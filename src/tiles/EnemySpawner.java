package tiles;

import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.RespawnableEnemy;
import main.Camera;
import main.Game;
import main.GameContext;
import tiles.resources.ResourceJob;
import tool.editor.TilePartLoader;

import java.util.function.Function;

public abstract class EnemySpawner extends TilePart implements JobActor {
    protected final GameContext context;
    protected final int ticksToRespawn;
    public EnemySpawner(GameContext context, int ticksToRespawn) {
        this.context = context;
        this.ticksToRespawn = ticksToRespawn;
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
}
