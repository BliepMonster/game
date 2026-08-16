package quests.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import quests.Quest;
import quests.QuestRegistry;
import quests.QuestTracker;

import java.awt.Color;
import java.util.List;

public class QuestMenuComponent extends Component {
    public List<Quest> quests;
    public final QuestTracker tracker;
    public final GameContext context;
    public final QuestPanel panel;
    public final Font font;
    public int lp = 0;
    private final QuestMenuSelectorBar selectorBar;
    private QuestMenuPage page;
    public QuestMenuComponent(QuestTracker tracker, GameContext context, QuestPanel panel, Font font) {
        super(CenterConstraint.CENTER, new RelativeYConstraint(-.7f), new RelativeSizeConstraint(1f), new RelativeSizeConstraint(1.2f));
        this.quests = QuestRegistry.all().stream().filter(q -> q.isUnlocked(tracker) && !tracker.isClaimed(q.getId())).toList();
        this.tracker = tracker;
        this.context = context;
        this.panel = panel;
        this.font = font;
        this.selectorBar = new QuestMenuSelectorBar(font, this);
        switchPage(0);
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        List<Quest> active = QuestRegistry.all().stream().filter(q -> q.isUnlocked(tracker) && !tracker.isClaimed(q.getId())).toList();
        if (!active.equals(quests)) {
            quests = active;
            selectorBar.recompute();
            switchPage(0);
        }
        selectorBar.manage(draw, pressed);
        page.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.BLACK, x, y, w, h);
    }
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}
    public void switchPage(int page) {
        if (page > (quests.size()-1)/5 || page < 0) return;
        lp = page;
        this.page = new QuestMenuPage(quests, lp * 5, tracker, context, font, panel);
    }
}
