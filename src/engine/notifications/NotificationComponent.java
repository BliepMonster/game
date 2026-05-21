package engine.notifications;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.Color;

import static java.awt.Color.black;

public class NotificationComponent extends Component {
    long deleteTime;
    private final String text;
    private final Color color;
    private final Font font;
    public NotificationComponent(Font font, String text, Color color, long deleteTime, PositionConstraint y) {
        super(new CenterConstraint(), y, new RelativeSizeConstraint(.4f), new RelativeSizeConstraint(.1f));
        this.text = text;
        this.color = color;
        this.deleteTime = deleteTime;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(color, x, y, w, h);
        float fsize = Math.min(font.getSizeForWidth(text, w), font.getSizeForHeight(h));
        float hh = font.getHeight(fsize);
        float ww = font.getWidthForSize(text, fsize);
        draw.string(text, black, x+w/2-ww/2, y+h/2-hh/2, font, fsize);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            deleteTime = 0; // delete
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
