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

public class UpdatingFixedFontSizeLabel extends Component {
    private float size;
    private Color color;
    private String text;
    private Font font;

    private float cachedArgHeight, cachedArgWidth;

    public UpdatingFixedFontSizeLabel(PositionConstraint x, PositionConstraint y, float size, Color color, String text, Font font) {
        super(x, y, new FullWidthConstraint(), new FullWidthConstraint());
        this.size = size;
        this.color = color;
        this.text = text;
        this.font = font;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.string(text, color, x, y, font, size);
    }
    public void detectClick(Screen screen, float x, float y) {}

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
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
        this.size = size;
        updateCache();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
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
    }
}
