package engine.gui.components.labels;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;
import engine.core.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class Paragraph extends Component {
    private Color color;
    private @Nullable String text;
    private float fontSize;
    private @NotNull Font font;
    public Paragraph(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, @Nullable String text, Color color, @NotNull Font font, float fontSize) {
        super(x, y, width, height);
        this.fontSize = fontSize;
        this.text = text;
        this.color = color;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.paragraph(text, color, x, y+height-font.getHeight(fontSize)/2, font, fontSize, width);
    }
    public void detectClick(Screen screen, float x, float y) {

    }
    public void detectContinuousPress(Screen screen, float x, float y) {

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public @Nullable String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public @NotNull Font getFont() {
        return font;
    }

    public void setFont(@NotNull Font font) {
        this.font = font;
    }
}
