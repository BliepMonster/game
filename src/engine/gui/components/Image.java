package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

public class Image extends Component {
    protected Texture texture;
    public Image(Texture texture, PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        super(x, y, width, height);
        this.texture = texture;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.image(texture, x, y, width, height);
    }
    public void detectClick(Screen screen, float x, float y) {
        //don't use this
    }
    public void detectContinuousPress(Screen screen, float x, float y) {
        //don't use this
    }
}
    