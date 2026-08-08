package tiles.house;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.*;

public class TransferGridComponent extends Component {
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    private final ChestWidget owner;
    private final boolean chest;
    private final int rows, cols;

    public TransferGridComponent(ChestWidget owner, boolean chest, float leftEdge, int rows, int cols) {
        super(new RelativeYConstraint(leftEdge), CenterConstraint.CENTER,
                new RelativeSizeConstraint(SPACING*(cols+1)+cols*ITEM_SIZE),
                new RelativeSizeConstraint(SPACING*(rows+1)+rows*ITEM_SIZE));
        this.owner = owner;
        this.chest = chest;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        for (int i = 0; i < rows*cols; ++i) {
            int row = i / cols;
            int col = i % cols;
            float xx = x + col*ITEM_SIZE + (col+1)*SPACING;
            float yy = y + row*ITEM_SIZE + (row+1)*SPACING;
            var item = owner.itemAt(chest, i);
            boolean selected = owner.isSelected(chest, i);
            draw.rect(selected ? Color.red : item == null ? Color.darkGray : Color.blue, xx, yy, ITEM_SIZE, ITEM_SIZE);
            if (item != null) {
                draw.image(item.definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
            }
        }
    }

    private int getCol(Screen screen, float x) {
        float left = getXConstraint().evaluate(screen, getWidthConstraint().evaluate(screen));
        float col = (x-left-SPACING)/(ITEM_SIZE+SPACING);
        if (col < 0 || col >= cols) return -1;
        return (int) col;
    }

    private int getRow(Screen screen, float r) {
        float h = -getHeightConstraint().evaluate(screen)/2;
        float base = h+SPACING;
        float stride = ITEM_SIZE+SPACING;
        float row = (r-base)/stride;
        if (row < 0 || row >= rows) return -1;
        return (int) row;
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        int col = getCol(screen, x);
        if (col == -1) return;
        int row = getRow(screen, y);
        if (row == -1) return;
        owner.clickSlot(chest, row*cols+col);
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
