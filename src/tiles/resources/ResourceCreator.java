package tiles.resources;

import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.saving.objects.SavedObject;
import main.Camera;
import main.Game;
import main.GameContext;
import main.YieldUtils;
import tiles.TilePart;

public abstract class ResourceCreator extends TilePart implements JobActor {
    public final ResourceDefinition definition;
    protected boolean ready = true;
    public ResourceCreator(ResourceDefinition definition) {
        this.definition = definition;
    }
    public void onClick(GameContext context) {
        use(context);
    }
    public void use(GameContext context) {
        if (ready) {
            ready = false;
            var player = context.getPlayer();
            player.inventory.add(this.definition.itemID(), YieldUtils.getYieldEffect(this.definition.yield(), player.stats.get(this.definition.modifier())));
            player.skills.giveXP(definition.skillID(), definition.xp());
            JobSystem.addJob(new ResourceJob(this, (float) definition.respawnTicks()/ Game.TICKS_PER_SECOND));
        }
    }
    public void penalty(GameContext context, float penalty) {
        if (ready) {
            ready = false;
            var player = context.getPlayer();
            player.inventory.add(this.definition.itemID(), YieldUtils.getYieldEffect(this.definition.yield(), player.stats.get(this.definition.modifier())*penalty));
            player.skills.giveXP(definition.skillID(), definition.xp());
            JobSystem.addJob(new ResourceJob(this, (float) definition.respawnTicks()/ Game.TICKS_PER_SECOND));
        }
    }
    public void notify(Job job) {
        ready = true;
    }
    public void onStand(GameContext context) {}
    public abstract void renderUnready(Camera camera, int x, int y);
    public abstract void renderReady(Camera camera, int x, int y);

    @Override
    public void render(int x, int y, Camera camera) {
        if (ready) renderReady(camera, x, y);
        else renderUnready(camera, x, y);
    }
}
