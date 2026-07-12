package tiles.crafting.scrolls;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

public class ScrollApplicationPageSelectorBar extends Component {
    public final int count;
    public int current;

    public final ScrollApplicationPageSelectorPiece piece = new ScrollApplicationPageSelectorPiece();

    public ScrollApplicationPageSelectorBar(int count) {
        super(new RelativeYConstraint(-.3f), new RelativeYConstraint(-.4f), new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.1f));
        this.count = count;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        for (int i = -2; i < 3; ++i) {
            int j = i+current;
            if (j < 0 || j >= count)
                continue;
            piece.tick(x+.175f*(i+2), y, draw, j+1, current == j);
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        for (int i = -2; i < 3; ++i) {
            int j = i+current;
            if (j < 0 || j >= count)
                continue;
            float px = .175f*(i+2)-.3f;
            if (x > px && x < px + .1f) swapPage(j);
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
    public void swapPage(int page) {
        this.current = page;
    }
}
