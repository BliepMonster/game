package tiles.house;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Input;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import org.lwjgl.glfw.GLFW;
import stats.inventory.items.Item;
import stats.inventory.items.ItemInventory;

import java.awt.*;

public class ChestWidget extends Component {
    private static final float CHEST_LEFT = -1.525f;
    private static final float PLAYER_LEFT = 0.125f;
    private final ItemInventory inventory;
    private final ChestTilePart chest;
    private final TransferGridComponent chestGrid;
    private final TransferGridComponent playerGrid;
    private boolean fromChest;
    private int selectedSlot = -1;

    public ChestWidget(ItemInventory inventory, ChestTilePart chest) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(100f), new RelativeSizeConstraint(1.3f));
        this.inventory = inventory;
        this.chest = chest;
        this.chestGrid = new TransferGridComponent(this, true, CHEST_LEFT, ChestTilePart.NUM_ROWS, ChestTilePart.NUM_ITEMS_PER_ROW);
        this.playerGrid = new TransferGridComponent(this, false, PLAYER_LEFT, ItemInventory.NUM_ROWS, ItemInventory.NUM_ITEMS_PER_ROW);
    }

    Item itemAt(boolean chest, int slot) {
        return chest ? this.chest.get(slot) : inventory.get(slot);
    }
    void setItem(boolean chest, int slot, Item item) {
        if (chest) this.chest.set(slot, item);
        else inventory.set(slot, item);
    }
    boolean isSelected(boolean chest, int slot) {
        return fromChest == chest && selectedSlot == slot;
    }
    void clickSlot(boolean chest, int slot) {
        if (selectedSlot == -1) {
            if (itemAt(chest, slot) != null) {
                fromChest = chest;
                selectedSlot = slot;
            }
        } else {
            var held = itemAt(fromChest, selectedSlot);
            var target = itemAt(chest, slot);
            setItem(fromChest, selectedSlot, target);
            setItem(chest, slot, held);
            selectedSlot = -1;
        }
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        chestGrid.manage(draw, pressed);
        playerGrid.manage(draw, pressed);
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) close();
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.gray, x, y, width, height);
        draw.rect(Color.red, draw.getScreen().getAspectRatio()-.1f, .55f, .1f, .1f);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (x > .55f && y > .55f && isInBounds(screen, x, y)) close();
    }

    void close() {
        GUI.scheduleClear(0);
        GUI.toggle(62);
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}
}
