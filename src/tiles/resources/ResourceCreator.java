package tiles.resources;

import engine.core.DrawManager;
import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.saving.objects.SavedObject;
import main.Game;
import main.GameContext;
import main.YieldUtils;
import tiles.TilePart;

public abstract class ResourceCreator extends TilePart implements JobActor {
    public final ResourceDefinition definition;
    private boolean ready = true;
    public ResourceCreator(ResourceDefinition definition) {
        this.definition = definition;
    }
    public void onClick(GameContext context) {
        if (ready) {
            ready = false;
            var player = context.getPlayer();
            player.inventory.add(this.definition.itemID(), YieldUtils.getYieldEffect(this.definition.yield(), player.stats.get(this.definition.modifier())));
            player.skills.giveXP(definition.skillID(), definition.xp());
            JobSystem.addJob(new ResourceJob(this, (float) definition.respawnTicks()/ Game.TICKS_PER_SECOND));
        }
    }
    public SavedObject save() {
        throw new UnsupportedOperationException();
    }
    public void notify(Job job) {
        ready = true;
    }
    public void onStand(GameContext context) {}
    public abstract void renderUnready(DrawManager draw, float x, float y, float w, float h);
    public abstract void renderReady(DrawManager draw, float x, float y, float w, float h);

    @Override
    public void render(DrawManager draw, float x, float y, float w, float h) {
        if (ready) renderReady(draw, x, y, w, h);
        else renderUnready(draw, x, y, w, h);
    }
}
