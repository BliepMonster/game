package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class Checkbox extends Component{
    private boolean enabled = false;
    private Color outer;
    private Color inner;

    public Checkbox(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, Color outer, Color inner) {
        super(x, y, width, height);
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(outer, x+width*9f/10f, y, width/10f, height);
        draw.rect(outer, x, y+height*9f/10f, width, height/10f);
        draw.rect(outer, x, y, width/10f, height);
        draw.rect(outer, x, y, width, height/10f);
        if (enabled) {
            draw.rect(inner, x+width/5f, y+height/5f, 3f/5f*width, 3f/5f*height);
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            enabled = !enabled;
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}

    public void setOuterColor(Color outer) {
        this.outer = outer;
    }

    public void setInnerColor(Color inner) {
        this.inner = inner;
    }
    public boolean isEnabled() {
        return enabled;
    }
}
