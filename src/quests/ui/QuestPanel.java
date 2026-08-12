package quests.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import quests.Quest;
import quests.QuestTracker;

import java.awt.Color;

public class QuestPanel extends Component {
    enum State {
        MENU,
        SELECTED
    }
    private final QuestMenuComponent menu;
    private QuestDetailComponent detail;
    private final QuestTracker tracker;
    private final GameContext context;
    private State state = State.MENU;
    private final Font font;
    public static final float HEIGHT = 1.4f;
    public static final float WIDTH = 1;
    public final int uiIndex;
    public QuestPanel(QuestTracker tracker, GameContext context, Font font, int uiIndex) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(WIDTH), new RelativeSizeConstraint(HEIGHT));
        this.tracker = tracker;
        this.context = context;
        this.font = font;
        this.uiIndex = uiIndex;
        this.menu = new QuestMenuComponent(tracker, context, this, font);
    }
    void select(Quest quest) {
        tracker.select(quest);
        this.detail = new QuestDetailComponent(quest, context, tracker, font);
        this.state = State.SELECTED;
    }
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        if (state == State.SELECTED) detail.manage(draw, pressed);
        else if (state == State.MENU) menu.manage(draw, pressed);
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        draw.rect(Color.red, x, y+h-.2f, .2f, .2f);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            if (y > HEIGHT/2-.2f && x < -WIDTH/2+.2f) {
                if (state == State.SELECTED)
                    state = State.MENU;
                else {
                    GUI.scheduleClear(uiIndex);
                    GUI.toggle(62);
                }
            }
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
