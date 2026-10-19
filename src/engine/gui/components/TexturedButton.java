package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

public abstract class TexturedButton extends Component {
    protected Texture texture;

    public TexturedButton(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, Texture texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    public void display(DrawManager draw, float x, float y, float width, float height) {
        displayWith(draw, x, y, width, height, texture);
    }
    protected void displayWith(DrawManager draw, float x, float y, float width, float height, Texture t) {
        draw.image(t, x, y, width, height);
    }
    public abstract void invoke();
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            invoke();
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
