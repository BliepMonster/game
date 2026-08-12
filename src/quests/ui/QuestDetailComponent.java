package quests.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import quests.Objective;
import quests.ObjectiveReward;
import quests.Quest;
import quests.QuestTracker;

import java.awt.Color;

public class QuestDetailComponent extends Component {
    private static final float CLAIM_HEIGHT = .15f;
    private final Quest quest;
    private final GameContext context;
    private final QuestTracker tracker;
    private final Font font;
    public QuestDetailComponent(Quest quest, GameContext context, QuestTracker tracker, Font font) {
        super(CenterConstraint.CENTER, new RelativeYConstraint(-.7f), new RelativeSizeConstraint(1f), new RelativeSizeConstraint(1.2f));
        this.quest = quest;
        this.context = context;
        this.tracker = tracker;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        Objective[] objs = quest.getObjectives();
        ObjectiveReward[] rewards = quest.getRewards();
        int extraLines = rewards == null ? 0 : rewards.length;
        float fsize = Math.min(font.getSizeForHeight((h-CLAIM_HEIGHT)/(objs.length+extraLines+2)), font.getSizeForHeight(.1f));
        float hh = font.getHeight(fsize);
        draw.string(quest.getName(), Color.cyan, x, y+h-hh, font, fsize);
        for (int i = 0; i < objs.length; ++i) {
            var obj = objs[i];
            int progress = Math.min(obj.getProgress(context), obj.getTotalTasks(context));
            String s = obj.getName() + " (" + progress + "/" + obj.getTotalTasks(context) + ")";
            draw.string(s, obj.isDone(context) ? Color.green : Color.white, x, y+h-(i+2)*hh, font, fsize);
        }
        if (rewards != null && rewards.length > 0) {
            for (int i = 0; i < rewards.length; ++i) {
                draw.string("Reward: " + rewards[i].getDescription(), Color.yellow, x, y+h-(objs.length+2+i)*hh, font, fsize);
            }
        }
        if (rewards != null && rewards.length > 0 && !tracker.isClaimed(quest.getId())) {
            Color claimColor = tracker.isClaimed(quest.getId()) ? Color.green : Color.orange;
            draw.rect(claimColor, x, y, w, CLAIM_HEIGHT);
            String claimText = tracker.isClaimed(quest.getId()) ? "Claimed" : "Claim";
            float csize = Math.min(font.getSizeForWidth(claimText, w), font.getSizeForHeight(CLAIM_HEIGHT));
            draw.string(claimText, Color.black, x + (w - font.getWidthForSize(claimText, csize))/2, y + (CLAIM_HEIGHT - font.getHeight(csize))/2, font, csize);
        }
    }
    public void detectClick(Screen screen, float x, float y) {
        if (quest.getRewards() == null || quest.getRewards().length == 0) return;
        float w = width.evaluate(screen);
        float h = height.evaluate(screen);
        float cx = this.x.evaluate(screen, w);
        float cy = this.y.evaluate(screen, h);
        if (x >= cx && x <= cx+w && y >= cy && y <= cy+CLAIM_HEIGHT && tracker.isCompleted(quest.getId()) && !tracker.isClaimed(quest.getId())) {
            tracker.claim(quest.getId(), context);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
