package tiles;

import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.json.JsonString;
import engine.json.JsonValue;
import engine.saving.objects.SavedObject;
import entities.RespawnableEnemy;
import main.Camera;
import main.Game;
import main.GameContext;
import tiles.resources.ResourceJob;
import tool.editor.TilePartLoader;

import java.util.function.Function;

public class EnemySpawner extends TilePart implements JobActor {
    private final Function<EnemySpawner, RespawnableEnemy> creator;
    private final GameContext context;
    private final int ticksToRespawn;
    public EnemySpawner(Function<EnemySpawner, RespawnableEnemy> creator, GameContext context, int ticksToRespawn) {
        this.creator = creator;
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
    public void notify(Job job) {
        context.spawn(creator.apply(this));
    }
    private void applyRespawnJob() {
        JobSystem.addJob(new ResourceJob(this, ticksToRespawn/(float) Game.TICKS_PER_SECOND));
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_SPAWNER);
    }
}
