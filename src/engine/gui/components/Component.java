package engine.gui.components;

import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Input;
import org.lwjgl.glfw.GLFW;

public abstract class Component {
    protected PositionConstraint x, y;
    protected SizeConstraint width, height;

    public Component(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PositionConstraint getYConstraint() {
        return y;
    }

    public void setYConstraint(PositionConstraint y) {
        this.y = y;
    }

    public PositionConstraint getXConstraint() {
        return x;
    }

    public void setXConstraint(PositionConstraint x) {
        this.x = x;
    }

    public SizeConstraint getWidthConstraint() {
        return width;
    }

    public void setWidthConstraint(SizeConstraint width) {
        this.width = width;
    }

    public SizeConstraint getHeightConstraint() {
        return height;
    }

    public void setHeightConstraint(SizeConstraint height) {
        this.height = height;
    }

    public void manage(DrawManager draw, boolean pressed) {
        Screen screen = draw.getScreen();
        float argWidth = width.evaluate(screen);
        float argHeight = height.evaluate(screen);
        if (pressed) {
            detectClick(screen, Input.getMouseX(screen), Input.getMouseY(screen));
        } if (Input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
            detectContinuousPress(screen, Input.getMouseX(screen), Input.getMouseY(screen));
        }
        display(draw, x.evaluate(screen, argWidth), y.evaluate(screen, argHeight), argWidth, argHeight);
    }
    public abstract void display(DrawManager draw, float x, float y, float width, float height);
    public abstract void detectClick(Screen screen, float x, float y);
    public abstract void detectContinuousPress(Screen screen, float x, float y);
    public void free() {}
    public boolean isInBounds(Screen screen, float x, float y) {
        float widthVal = width.evaluate(screen);
        float heightVal = height.evaluate(screen);
        float xPos = this.x.evaluate(screen, widthVal);
        float yPos = this.y.evaluate(screen, heightVal);
        return x >= xPos && x <= xPos+widthVal && y >= yPos && y <= yPos+heightVal;
    }
}
