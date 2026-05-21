package engine.gui.components.sliders;

import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

import java.awt.*;

public class IntegerSlider extends Slider {
    public IntegerSlider(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, float value, float min, float max, Color innerSliderColor, Color outerSliderColor, Color innerSelectorColor, Color outerSelectorColor) {
        super(x, y, width, height, value, min, max, innerSliderColor, outerSliderColor, innerSelectorColor, outerSelectorColor);
    }
    public IntegerSlider(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, float min, float max, Color innerSliderColor, Color outerSliderColor, Color innerSelectorColor, Color outerSelectorColor) {
        super(x, y, width, height, min, max, innerSliderColor, outerSliderColor, innerSelectorColor, outerSelectorColor);
    }
    public void setValue(float value) {
        this.value = Math.round(value);
    }
}
