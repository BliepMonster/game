package stats.inventory.items.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;

import java.awt.*;

import static stats.inventory.items.ItemInventory.*;

public class InventoryGridComponent extends Component {
    private final Player player;
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    private int selectedSlot = -1;
    public InventoryGridComponent(Player player) {
        super(new CenterConstraint(), new CenterConstraint(), new RelativeSizeConstraint(SPACING*(NUM_ITEMS_PER_ROW+1)+NUM_ITEMS_PER_ROW*ITEM_SIZE), new RelativeSizeConstraint(SPACING*(NUM_ROWS+1)+NUM_ROWS*ITEM_SIZE));
        this.player = player;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        for (int i = 0; i < NUM_ITEMS; ++i) {
            int row = i / NUM_ITEMS_PER_ROW;
            int col = i % NUM_ITEMS_PER_ROW;
            float xx = x + col * ITEM_SIZE + (col+1) * SPACING;
            float yy = y + row * ITEM_SIZE + (row+1) * SPACING;
            draw.rect(i == selectedSlot ? Color.red : row == 0 ? Color.green : Color.blue, xx, yy, ITEM_SIZE, ITEM_SIZE);
            if (player.items.get(i) == null) continue;
            draw.image(player.items.get(i).definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
        }
    }
    // should refactor later
    private int getX(float x) {
        return switch (x) {
            case float f when f < -.55f && f > -.65f -> 0;
            case float f when f < -.4f && f > -.5f -> 1;
            case float f when f < -.25f && f > -.35f -> 2;
            case float f when f < -.1f && f > -.2f -> 3;
            case float f when f < .05f && f > -.05f -> 4;
            case float f when f < .2f && f > .1f -> 5;
            case float f when f < .35f && f > .25f -> 6;
            case float f when f < .5f && f > .4f -> 7;
            case float f when f < .65f && f > .55f -> 8;
            default -> -1;
        };
    }
    private int getY(Screen screen, float r) {
        float h  = -this.getHeightConstraint().evaluate(screen)/2;
        float base = h+SPACING;
        float stride = ITEM_SIZE+SPACING;
        float row = (r-base)/stride;
        if (row < 0 || row >= NUM_ROWS) return -1;
        return (int) row;
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            int slot = getX(x);
            if (slot == -1) {
                selectedSlot = -1;
                return;
            }
            int s2 = getY(screen, y);
            if (s2 == -1) {
                selectedSlot = -1;
                return;
            }
            int pos = s2*NUM_ITEMS_PER_ROW+slot;
            if (selectedSlot == -1 && player.items.get(pos) != null)
                selectedSlot = pos;
            else {
                if (selectedSlot == -1) return;
                player.items.swap(selectedSlot, pos);
                selectedSlot = -1;
            }
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
