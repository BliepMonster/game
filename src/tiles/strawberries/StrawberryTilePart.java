package tiles.strawberries;

import engine.core.DrawManager;
import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.jobs.JobSystem;
import engine.saving.objects.SavedObject;
import main.GameContext;
import main.ItemConstants;
import main.YieldUtils;
import stats.skills.Skill;
import stats.skills.SkillSet;
import tiles.TilePart;

import java.awt.*;

import static java.awt.Color.red;

public class StrawberryTilePart extends TilePart implements JobActor {
    private boolean ready = true;
    public static final Color green = new Color(0, 160, 0);
    public void render(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(green, x+w/4, y+h/4, w/2, h/2);
        if (ready) {
            draw.rect(red, x+w/3, y+h/3, w/3, h/3);
        }
    }
    public boolean isStandable() {
        return true; // players & entities can stand on strawberry bushes
    }
    public void onStand(GameContext context) {}
    public void onClick(GameContext context) {
        if (ready) {
            ready = false;
            var player = context.getPlayer();
            player.inventory.add(ItemConstants.ITEM_STRAWBERRY, YieldUtils.getYieldEffect(1, player.stats.foragingYield));
            player.skills.giveXP(SkillSet.SKILL_FORAGING, 4);
            JobSystem.addJob(new StrawberryCreationJob(this));
        }
    }
    public SavedObject save() {
        throw new UnsupportedOperationException();
    }
    public void notify(Job job) {
        ready = true;
    }
}
