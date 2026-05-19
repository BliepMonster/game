package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.graphics.TextMesh;
import engine.core.input.Input;
import engine.core.text.Font;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.FullWidthConstraint;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;

public class FixedFontSizeLabel extends Component {
    private float size;
    private Color color;
    private String text;
    private Font font;

    private float cachedArgHeight, cachedArgWidth;
    private TextMesh mesh;

    public FixedFontSizeLabel(PositionConstraint x, PositionConstraint y, float size, Color color, String text, Font font) {
        super(x, y, new FullWidthConstraint(), new FullWidthConstraint());
        this.size = size;
        this.color = color;
        this.text = text;
        this.font = font;
        this.mesh = new TextMesh(text, font, size, 1000000);
        mesh.create();
        updateCache();
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.renderAsText(mesh, font, color, x, y);
    }
    public void detectClick(Screen screen, float x, float y) {}

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (this.text.equals(text))
            return;
        this.text = text;
        updateCache();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        if (this.size == size)
            return;
        else if (size < 0)
            throw new IllegalArgumentException("Size cannot be negative");
        this.size = size;
        updateCache();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (this.font == font)
            return;
        this.font = font;
        updateCache();
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        Screen screen = draw.getScreen();
        display(draw, x.evaluate(screen, cachedArgWidth), y.evaluate(screen, cachedArgHeight), cachedArgWidth, cachedArgHeight);
        if (pressed) {
            detectClick(screen, Input.getMouseX(screen), Input.getMouseY(screen));
        } if (Input.isKeyDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
            detectContinuousPress(screen, Input.getMouseX(screen), Input.getMouseY(screen));
        }
    }
    private void updateCache() {
        cachedArgWidth = font.getWidthForSize(text, size);
        cachedArgHeight = font.getHeight(size);
        this.mesh.destroy();
        this.mesh = new TextMesh(text, font, size, 1000000);
        this.mesh.create();
    }
    public void free() {
        mesh.close();
    }
}
