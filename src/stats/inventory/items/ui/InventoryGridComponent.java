package stats.inventory.items.ui;

import combat.equipment.EquipmentSlot;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import stats.inventory.items.Item;

import java.awt.*;

import static stats.inventory.items.ItemInventory.*;

public class InventoryGridComponent extends Component {
    public record SelectedItem(boolean inventory, int slot) {
        public Item get(Player player) {
            if (inventory) return player.items.get(slot);
            return player.items.getEquipment(EquipmentSlot.values()[slot]);
        }
        public void set(Player player, Item item) {
            if (inventory) player.items.set(slot, item);
            else player.items.setEquipment(EquipmentSlot.values()[slot], item);
        }
    }
    private final Player player;
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    public static final float WIDTH = SPACING*(NUM_ITEMS_PER_ROW+1)+NUM_ITEMS_PER_ROW*ITEM_SIZE;
    public static final float EQUIPMENT_PANEL_WIDTH = 3*SPACING+ITEM_SIZE;
    private SelectedItem selectedSlot = null;
    public InventoryGridComponent(Player player) {
        super(new RelativeYConstraint(-WIDTH/2), CenterConstraint.CENTER, new RelativeSizeConstraint(WIDTH+EQUIPMENT_PANEL_WIDTH), new RelativeSizeConstraint(SPACING*(NUM_ROWS+1)+NUM_ROWS*ITEM_SIZE));
        this.player = player;
    }
    public boolean isSelected(boolean eq, int index) {
        if (this.selectedSlot == null) return false;
        return this.selectedSlot.equals(new SelectedItem(eq, index));
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        for (int i = 0; i < NUM_ITEMS; ++i) {
            int row = i / NUM_ITEMS_PER_ROW;
            int col = i % NUM_ITEMS_PER_ROW;
            float xx = x + col * ITEM_SIZE + (col+1) * SPACING;
            float yy = y + row * ITEM_SIZE + (row+1) * SPACING;
            draw.rect(isSelected(true, i) ? Color.red : row == 0 ? Color.green : Color.blue, xx, yy, ITEM_SIZE, ITEM_SIZE);
            if (player.items.get(i) == null) continue;
            draw.image(player.items.get(i).definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
        }
        // draw helmet
        var xx = x + WIDTH + 2*SPACING;
        draw.rect(isSelected(false, EquipmentSlot.HELMET.ordinal()) ? Color.red : Color.white, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var helmet = this.player.items.getEquipment(EquipmentSlot.HELMET);
        if (helmet != null) {
            draw.image(helmet.definition.texture, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.CHESTPLATE.ordinal()) ? Color.red : Color.white, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var chestplate = this.player.items.getEquipment(EquipmentSlot.CHESTPLATE);
        if (chestplate != null) {
            draw.image(chestplate.definition.texture, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.RING.ordinal()) ? Color.red : Color.white, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var ring = this.player.items.getEquipment(EquipmentSlot.RING);
        if (ring != null) {
            draw.image(ring.definition.texture, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.LEGGINGS.ordinal()) ? Color.red : Color.white, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var leggings = this.player.items.getEquipment(EquipmentSlot.LEGGINGS);
        if (leggings != null) {
            draw.image(leggings.definition.texture, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.BOOTS.ordinal()) ? Color.red : Color.white, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
        var boots = this.player.items.getEquipment(EquipmentSlot.BOOTS);
        if (boots != null) {
            draw.image(boots.definition.texture, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
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
            case float f when f < WIDTH/2+2*SPACING+ITEM_SIZE && f > WIDTH/2+2*SPACING -> -2;
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
                selectedSlot = null;
                return;
            }
            int s2 = getY(screen, y);
            if (s2 == -1) {
                selectedSlot = null;
                return;
            }
            if (slot == -2) {
                var enumV = switch (s2) {
                    case 0 -> EquipmentSlot.BOOTS;
                    case 1 -> EquipmentSlot.LEGGINGS;
                    case 2 -> EquipmentSlot.RING;
                    case 3 -> EquipmentSlot.CHESTPLATE;
                    case 4 -> EquipmentSlot.HELMET;
                    default -> throw new RuntimeException("this should never happen");
                };
                switchWithEquipment(enumV);
            } else {
                switchWithItem(s2*NUM_ITEMS_PER_ROW+slot);
            }
        }
    }
    public void detectContinuousPress(Screen screen, float x, float y) {}
    void switchWithEquipment(EquipmentSlot slot) {
        if (this.selectedSlot == null && player.items.getEquipment(slot) != null) {
            selectedSlot = new SelectedItem(false, slot.ordinal());
        } else if (this.selectedSlot == null) {} else {
            var item = selectedSlot.get(player);
            if (item.definition.getEquipmentSlot() != slot) {
                selectedSlot = null;
                return;
            }
            selectedSlot.set(player, player.items.getEquipment(slot));
            player.items.setEquipment(slot, item);
            selectedSlot = null;
        }
    }
    void switchWithItem(int index) {
        if (this.selectedSlot == null && player.items.get(index) != null) {
            selectedSlot = new SelectedItem(true, index);
        } else if (this.selectedSlot == null) {} else {
            var item = selectedSlot.get(player);
            selectedSlot.set(player, player.items.get(index));
            player.items.set(index, item);
            selectedSlot = null;
        }
    }
}
