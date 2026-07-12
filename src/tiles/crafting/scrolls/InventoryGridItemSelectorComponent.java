package tiles.crafting.scrolls;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Input;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static stats.inventory.items.ItemInventory.*;

public class InventoryGridItemSelectorComponent extends Component {
    private final ScrollApplicationWidget owner;
    private final Player player;
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    public static final float WIDTH = SPACING*(NUM_ITEMS_PER_ROW+1)+NUM_ITEMS_PER_ROW*ITEM_SIZE;

    public InventoryGridItemSelectorComponent(ScrollApplicationWidget owner, Player player) {
        super(new RelativeYConstraint(-WIDTH/2), CenterConstraint.CENTER,
              new RelativeSizeConstraint(WIDTH), new RelativeSizeConstraint(SPACING*(NUM_ROWS+1)+NUM_ROWS*ITEM_SIZE));
        this.owner = owner;
        this.player = player;
    }

    public void display(DrawManager draw, float x, float y, float w, float h) {
        if (Input.isKeyDown(GLFW.GLFW_KEY_TAB)) owner.state = ScrollApplicationWidget.State.OPEN;
        draw.rect(Color.black, x, y, w, h);
        for (int i = 0; i < NUM_ITEMS; ++i) {
            int row = i / NUM_ITEMS_PER_ROW;
            int col = i % NUM_ITEMS_PER_ROW;
            float xx = x + col * ITEM_SIZE + (col+1) * SPACING;
            float yy = y + row * ITEM_SIZE + (row+1) * SPACING;
            var item = player.items.get(i);
            boolean eligible = item != null && item.definition.canApplyScrolls();
            draw.rect(eligible ? Color.blue : Color.darkGray, xx, yy, ITEM_SIZE, ITEM_SIZE);
            if (eligible) {
                draw.image(item.definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
            }
        }
    }

    private int getCol(float x) {
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

    private int getRow(Screen screen, float r) {
        float h  = -this.getHeightConstraint().evaluate(screen)/2;
        float base = h+SPACING;
        float stride = ITEM_SIZE+SPACING;
        float row = (r-base)/stride;
        if (row < 0 || row >= NUM_ROWS) return -1;
        return (int) row;
    }

    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        int col = getCol(x);
        if (col == -1) return;
        int row = getRow(screen, y);
        if (row == -1) return;
        int index = row * NUM_ITEMS_PER_ROW + col;
        var item = player.items.get(index);
        if (item != null && item.definition.canApplyScrolls()) {
            owner.selected = item;
            owner.state = ScrollApplicationWidget.State.OPEN;
        }
    }

    public void detectContinuousPress(Screen screen, float x, float y) {}
}
