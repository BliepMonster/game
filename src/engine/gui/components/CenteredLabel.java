package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.text.Font;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.Color;

public class CenteredLabel extends Component {
    protected String text;
    protected Color color;
    protected Font font;

    public CenteredLabel(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, String text, Color color, Font font) {
        super(x, y, width, height);
        this.text = text;
        this.color = color;
        this.font = font;
    }

    public void display(DrawManager draw, float x, float y, float width, float height) {
        float size = Math.min(font.getSizeForWidth(text, width), font.getSizeForHeight(height));
        float dWidth = font.getWidthForSize(text, size);
        draw.string(text, color, x-dWidth/2, y, font, size);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {}
    public void detectContinuousPress(Screen screen, float x, float y) {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
