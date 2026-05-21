package engine.gui.components.sliders;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public abstract class Slider extends Component {
    protected float value,
            min,
            max;
    protected Color innerSliderColor,
            outerSliderColor,
            innerSelectorColor,
            outerSelectorColor;

    public Slider(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, float value, float min, float max, Color innerSliderColor, Color outerSliderColor, Color innerSelectorColor, Color outerSelectorColor) {
        super(x, y, width, height);
        this.value = value;
        this.min = min;
        this.innerSliderColor = innerSliderColor;
        this.max = max;
        this.outerSliderColor = outerSliderColor;
        this.innerSelectorColor = innerSelectorColor;
        this.outerSelectorColor = outerSelectorColor;
    }
    public Slider(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, float min, float max, Color innerSliderColor, Color outerSliderColor, Color innerSelectorColor, Color outerSelectorColor) {
        super(x, y, width, height);
        this.min = min;
        this.innerSliderColor = innerSliderColor;
        this.max = max;
        this.outerSliderColor = outerSliderColor;
        this.innerSelectorColor = innerSelectorColor;
        this.outerSelectorColor = outerSelectorColor;
        this.value = min;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        float drawH = (float) (0.05/draw.getScreen().getAspectRatio());
        float innerH = drawH/3;
        draw.rect(outerSliderColor, x, y+(h/2)-drawH/2, w, drawH);
        draw.rect(innerSliderColor, x, y+(h/2)-innerH/2, w, innerH);
        float ratio = (value-min)/(max-min);
        float mid = ratio*w+x;
        draw.rect(outerSelectorColor, mid-drawH/2, (float) (y+(h/2)-1.5*drawH), drawH, 3*drawH);
        draw.rect(innerSelectorColor, mid-innerH/2, (float) (y+(h/2)-1.5*drawH), innerH, 3*drawH);
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            float width = getWidthConstraint().evaluate(screen);
            float cx = getXConstraint().evaluate(screen, width);
            float ratio = (x-cx)/width;
            setValue(ratio*(max-min)+min);
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {
        detectClick(screen, x, y);
    }
    public abstract void setValue(float value);
    public float getValue() {
        return value;
    }
}
