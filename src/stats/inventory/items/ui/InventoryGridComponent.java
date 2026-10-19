package stats.inventory.items.ui;

import combat.equipment.EquipmentSlot;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.core.input.Input;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import main.GameContext;
import main.TextureIdMap;
import stats.inventory.items.Item;

import java.awt.*;

import static stats.inventory.items.ItemInventory.*;

public class InventoryGridComponent extends Component {
    public record SelectedItem(boolean inventory, int slot) {
        public Item get(Player player) {
            if (inventory) return player.items.get(slot);
            return player.items.getEquipment(EquipmentSlot.values()[slot]);
        }
        public boolean canSet(Item item) {
            if (!inventory && item != null) return item.definition.getEquipmentSlot().ordinal() == slot;
            return true;
        }
        public void set(Player player, Item item) {
            if (inventory) player.items.set(slot, item);
            else player.items.setEquipment(EquipmentSlot.values()[slot], item);
        }
    }
    private final Player player;
    public static final Texture ICON_HELMET = TextureIdMap.get("base:icon:helmet");
    public static final Texture ICON_BOOTS = TextureIdMap.get("base:icon:boots");
    public static final Texture ICON_CHESTPLATE = TextureIdMap.get("base:icon:chestplate");
    public static final Texture ICON_LEGGINGS = TextureIdMap.get("base:icon:leggings");
    public static final Texture ICON_RING = TextureIdMap.get("base:icon:ring");
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    public static final float EQUIPMENT_ICON_SIZE = ITEM_SIZE*4/5;
    public static final float EQUIPMENT_ICON_OFFSET = (ITEM_SIZE-EQUIPMENT_ICON_SIZE)/2;
    public static final float WIDTH = SPACING*(NUM_ITEMS_PER_ROW+1)+NUM_ITEMS_PER_ROW*ITEM_SIZE;
    public static final float EQUIPMENT_PANEL_WIDTH = 3*SPACING+ITEM_SIZE;
    private SelectedItem selectedSlot = null;
    private final GameContext context;
    public InventoryGridComponent(GameContext context) {
        super(new RelativeYConstraint(-WIDTH/2), CenterConstraint.CENTER, new RelativeSizeConstraint(WIDTH+EQUIPMENT_PANEL_WIDTH), new RelativeSizeConstraint(SPACING*(NUM_ROWS+1)+NUM_ROWS*ITEM_SIZE));
        this.player = context.getPlayer();
        this.context = context;
    }
    public boolean isSelected(boolean eq, int index) {
        if (this.selectedSlot == null) return false;
        return this.selectedSlot.equals(new SelectedItem(eq, index));
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        super.manage(draw, pressed);
        var hovered = getHoveredSlot(draw.getScreen());
        var item = hovered == null ? null : hovered.get(player);
        if (item == null) {
            tooltip = null;
        } else if (tooltip == null) {
            tooltip = new ItemTooltipComponent(item, context);
        } else if (tooltip.item != item) {
            tooltip.text.close();
            tooltip = new ItemTooltipComponent(item, context);
        }
        if (tooltip != null) tooltip.manage(draw, pressed);
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
            if (!player.items.get(i).scrolls.stats.isEmpty() && !isSelected(true, i)) draw.rect(Color.magenta, xx, yy, ITEM_SIZE, ITEM_SIZE);
            draw.image(player.items.get(i).definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
        }
        // draw helmet
        var xx = x + WIDTH + 2*SPACING;
        draw.rect(isSelected(false, EquipmentSlot.HELMET.ordinal()) ? Color.red : Color.white, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var helmet = this.player.items.getEquipment(EquipmentSlot.HELMET);
        if (helmet != null) {
            draw.image(helmet.definition.texture, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        } else {
            draw.image(ICON_HELMET, xx+EQUIPMENT_ICON_OFFSET, y+SPACING*5+4*ITEM_SIZE+EQUIPMENT_ICON_OFFSET, EQUIPMENT_ICON_SIZE, EQUIPMENT_ICON_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.CHESTPLATE.ordinal()) ? Color.red : Color.white, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var chestplate = this.player.items.getEquipment(EquipmentSlot.CHESTPLATE);
        if (chestplate != null) {
            draw.image(chestplate.definition.texture, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        } else {
            draw.image(ICON_CHESTPLATE, xx+EQUIPMENT_ICON_OFFSET, y+SPACING*4+3*ITEM_SIZE+EQUIPMENT_ICON_OFFSET, EQUIPMENT_ICON_SIZE, EQUIPMENT_ICON_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.RING.ordinal()) ? Color.red : Color.white, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var ring = this.player.items.getEquipment(EquipmentSlot.RING);
        if (ring != null) {
            draw.image(ring.definition.texture, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        } else {
            draw.image(ICON_RING, xx+EQUIPMENT_ICON_OFFSET, y+SPACING*3+2*ITEM_SIZE+EQUIPMENT_ICON_OFFSET, EQUIPMENT_ICON_SIZE, EQUIPMENT_ICON_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.LEGGINGS.ordinal()) ? Color.red : Color.white, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var leggings = this.player.items.getEquipment(EquipmentSlot.LEGGINGS);
        if (leggings != null) {
            draw.image(leggings.definition.texture, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        } else {
            draw.image(ICON_LEGGINGS, xx+EQUIPMENT_ICON_OFFSET, y+SPACING*2+ITEM_SIZE+EQUIPMENT_ICON_OFFSET, EQUIPMENT_ICON_SIZE, EQUIPMENT_ICON_SIZE);
        }
        draw.rect(isSelected(false, EquipmentSlot.BOOTS.ordinal()) ? Color.red : Color.white, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
        var boots = this.player.items.getEquipment(EquipmentSlot.BOOTS);
        if (boots != null) {
            draw.image(boots.definition.texture, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
        } else {
            draw.image(ICON_BOOTS, xx+EQUIPMENT_ICON_OFFSET, y+SPACING+EQUIPMENT_ICON_OFFSET, EQUIPMENT_ICON_SIZE, EQUIPMENT_ICON_SIZE);
        }
    }
    // should refactor later
    private int getX(float x) {
        if (x < -.55f && x > -.65f) return 0;
        if (x < -.4f && x > -.5f) return 1;
        if (x < -.25f && x > -.35f) return 2;
        if (x < -.1f && x > -.2f) return 3;
        if (x < .05f && x > -.05f) return 4;
        if (x < .2f && x > .1f) return 5;
        if (x < .35f && x > .25f) return 6;
        if (x < .5f && x > .4f) return 7;
        if (x < .65f && x > .55f) return 8;
        if (x < WIDTH/2+2*SPACING+ITEM_SIZE && x > WIDTH/2+2*SPACING) return -2;
        return -1;
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
                EquipmentSlot enumV;
                if (s2 == 0) enumV = EquipmentSlot.BOOTS;
                else if (s2 == 1) enumV = EquipmentSlot.LEGGINGS;
                else if (s2 == 2) enumV = EquipmentSlot.RING;
                else if (s2 == 3) enumV = EquipmentSlot.CHESTPLATE;
                else if (s2 == 4) enumV = EquipmentSlot.HELMET;
                else throw new RuntimeException("this should never happen");
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
        } else if (this.selectedSlot != null) {
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
        } else if (this.selectedSlot != null) {
            if (!selectedSlot.canSet(player.items.get(index))) {
                selectedSlot = null;
                return;
            }
            var item = selectedSlot.get(player);
            selectedSlot.set(player, player.items.get(index));
            player.items.set(index, item);
            selectedSlot = null;
        }
    }
    private ItemTooltipComponent tooltip;
    public SelectedItem getHoveredSlot(Screen screen) {
        float x = Input.getMouseX(screen);
        float y = Input.getMouseY(screen);
        if (!isInBounds(screen, x, y)) return null;
        int slot = getX(x);
        if (slot == -1) return null;
        int s2 = getY(screen, y);
        if (s2 == -1) return null;
        if (slot == -2) {
            EquipmentSlot enumV;
            if (s2 == 0) enumV = EquipmentSlot.BOOTS;
            else if (s2 == 1) enumV = EquipmentSlot.LEGGINGS;
            else if (s2 == 2) enumV = EquipmentSlot.RING;
            else if (s2 == 3) enumV = EquipmentSlot.CHESTPLATE;
            else if (s2 == 4) enumV = EquipmentSlot.HELMET;
            else throw new RuntimeException("this should never happen");
            return new SelectedItem(false, enumV.ordinal());
        }
        return new SelectedItem(true, s2 * NUM_ITEMS_PER_ROW + slot);
    }
}
