package engine.gui.components;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Container extends Component implements Iterable<Component> {
    protected List<Component> parts = new ArrayList<>();
    protected Texture texture;
    public Container(Texture tex, PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height) {
        super(x,y,width,height);
        this.texture = tex;
    }
    public void display(DrawManager draw, float x, float y, float width, float height) {
        if (texture == null)
            return;
        draw.image(texture, x, y, width, height);
    }
    public List<Component> getComponents() {
        return parts;
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}

    @Override
    public void detectClick(Screen screen, float x, float y) {}

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        for (Component component : parts) {
            if (component == null)
                continue;
            component.manage(draw, pressed);
        }
    }
    public void add(Component c) {
        parts.add(c);
    }
    public void clear() {
        parts.clear();
    }
    public void remove(Component c) {
        parts.remove(c);
    }
    public Component getByPosition(int position) {
        return parts.get(position);
    }

    @Override
    public @NotNull Iterator<Component> iterator() {
        return parts.iterator();
    }

    public boolean isInBounds(Screen screen, float x, float y) {
        if (super.isInBounds(screen, x, y)) {
            return true;
        }
        for (Component component : parts) {
            if (component == null)
                continue;
            if (component.isInBounds(screen, x, y)) {
                return true;
            }
        }
        return false;
    }
}
