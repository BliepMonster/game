package tiles.resources;

import engine.core.DrawManager;
import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.saving.objects.SavedObject;
import main.Game;
import main.GameContext;
import main.YieldUtils;
import stats.StatHolder;
import stats.skills.Skill;
import tiles.TilePart;

public abstract class ResourceCreator extends TilePart implements JobActor {
    public final String itemID;
    public final int xp;
    private boolean ready = true;
    public final int yield;
    public final Skill skill;
    public StatHolder.Stat modifier;
    public int respawnTicks;
    public ResourceCreator(String itemID, int xp, Skill skill, StatHolder.Stat modifier, int respawnTicks, int yield) {
        this.itemID = itemID;
        this.xp = xp;
        this.skill = skill;
        this.modifier = modifier;
        this.respawnTicks = respawnTicks;
        this.yield = yield;
    }
    public void onClick(GameContext context) {
        if (ready) {
            ready = false;
            var player = context.getPlayer();
            player.inventory.add(this.itemID, YieldUtils.getYieldEffect(this.yield, player.stats.get(this.modifier)));
            skill.getXP(xp);
            JobSystem.addJob(new ResourceJob(this, (float) respawnTicks/ Game.TICKS_PER_SECOND));
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
