package engine.gui.components.labels;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.graphics.TextMesh;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;
import engine.core.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

public class Paragraph extends Component {
    private Color color;
    private @Nullable String text;
    private float fontSize;
    private @NotNull Font font;
    private TextMesh mesh;
    private boolean dirty = true;
    private float builtLimit;

    public Paragraph(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, @Nullable String text, Color color, @NotNull Font font, float fontSize) {
        super(x, y, width, height);
        this.fontSize = fontSize;
        this.text = text;
        this.color = color;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        if (dirty || width != builtLimit) rebuild(width);
        draw.renderAsText(mesh, font, color, x, y+height-font.getHeight(fontSize)/2);
    }
    private void rebuild(float widthLimit) {
        if (mesh != null) mesh.close();
        mesh = new TextMesh(text == null ? "null" : text, font, fontSize, widthLimit/100f);
        mesh.create();
        builtLimit = widthLimit;
        dirty = false;
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
        if (this.fontSize == fontSize) return;
        this.fontSize = fontSize;
        dirty = true;
    }

    public @Nullable String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        if (Objects.equals(this.text, text)) return;
        this.text = text;
        dirty = true;
    }

    public @NotNull Font getFont() {
        return font;
    }

    public void setFont(@NotNull Font font) {
        if (this.font == font) return;
        this.font = font;
        dirty = true;
    }

    @Override
    public void free() {
        if (mesh != null) {
            mesh.close();
            mesh = null;
            dirty = true;
        }
    }
}
