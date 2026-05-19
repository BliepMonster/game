package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Texture;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class TintedImage extends Image {
    protected Color color;
    public TintedImage(Color color, Texture texture, PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        super(texture, x, y, width, height);
        this.color = color;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.tintedImage(texture, color, x, y, width, height);
    }
}
