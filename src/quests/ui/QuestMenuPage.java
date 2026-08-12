package quests.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;
import quests.Quest;
import quests.QuestTracker;

import java.awt.Color;
import java.util.List;

public class QuestMenuPage extends Component {
    public final Quest[] quests;
    public final QuestTracker tracker;
    public final GameContext context;
    public final Font font;
    public final QuestPanel callbackPanel;
    public QuestMenuPage(List<Quest> quests, int index, QuestTracker tracker, GameContext context, Font font, QuestPanel panel) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(1), new RelativeSizeConstraint(1));
        var questsA = new Quest[Math.min(5, quests.size() - index)];
        int offset = 0;
        for (int i = 0; i < questsA.length; ++i) {
            if (index+i+offset >= quests.size()) {
                var questsB = new Quest[i];
                System.arraycopy(questsA, 0, questsB, 0, questsB.length);
                questsA = questsB;
                break;
            }
            questsA[i] = quests.get(index + i+offset);
            if (tracker.isClaimed(questsA[i].getId())) {
                offset++;
                i--;
            }
        }
        this.quests = questsA;
        this.tracker = tracker;
        this.context = context;
        this.font = font;
        this.callbackPanel = panel;
    }
    public static final float UNIT_SIZE = .2f;
    public void display(DrawManager draw, float x, float y, float w, float h) {
        var yy = y + h - UNIT_SIZE*(quests.length+1);
        draw.rect(Color.black, x, y, w, h);
        for (int i = quests.length-1; i >= 0; --i) {
            var quest = quests[i];
            yy += UNIT_SIZE;
            Color color;
            if (tracker.isSelected(quest)) color = Color.magenta;
            else if (tracker.isCompleted(quest.getId())) color = Color.green;
            else if (quest.isStarted(context)) color = Color.gray;
            else color = Color.black;
            draw.rect(color, x, yy, w, UNIT_SIZE);
            String name = quest.getName();
            float fsize = Math.min(font.getSizeForWidth(name, w), font.getSizeForHeight(UNIT_SIZE));
            draw.string(name, Color.white, x, yy, font, fsize);
        }
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            int index = (int) ((0.5-y) / UNIT_SIZE);
            if (index >= quests.length) return;
            var quest = quests[index];
            callbackPanel.select(quest);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
