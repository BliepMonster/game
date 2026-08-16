package crafting.ui;

import combat.equipment.EquipmentSlot;
import crafting.DeconstructorRegistry;
import crafting.FullItemStorage;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.position.RelativeYConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import stats.inventory.items.Item;
import tiles.npc.ResourceGiver;

import java.awt.*;

import static stats.inventory.items.ItemInventory.*;

public class DeconstructorPanel extends Component {
    private final Player player;
    public static final float SPACING = .05f;
    public static final float ITEM_SIZE = .1f;
    public static final float WIDTH = SPACING*(NUM_ITEMS_PER_ROW+1)+NUM_ITEMS_PER_ROW*ITEM_SIZE;
    public static final float EQUIPMENT_PANEL_WIDTH = 3*SPACING+ITEM_SIZE;
    public static final float HEIGHT = SPACING*(NUM_ROWS+1)+NUM_ROWS*ITEM_SIZE+.1f;
    public final long opened = System.currentTimeMillis();
    public DeconstructorPanel(Player player) {
        super(new RelativeYConstraint(-WIDTH/2), CenterConstraint.CENTER, new RelativeSizeConstraint(WIDTH+EQUIPMENT_PANEL_WIDTH), new RelativeSizeConstraint(HEIGHT));
        this.player = player;
    }
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(Color.black, x, y, w, h);
        draw.rect(Color.RED, x+w-.1f, y+h-.1f, .1f, .1f);
        for (int i = 0; i < NUM_ITEMS; ++i) {
            int row = i / NUM_ITEMS_PER_ROW;
            int col = i % NUM_ITEMS_PER_ROW;
            float xx = x + col * ITEM_SIZE + (col+1) * SPACING;
            float yy = y + row * ITEM_SIZE + (row+1) * SPACING;
            draw.rect(row == 0 ? Color.green : Color.blue, xx, yy, ITEM_SIZE, ITEM_SIZE);
            if (player.items.get(i) == null) continue;
            draw.image(player.items.get(i).definition.texture, xx, yy, ITEM_SIZE, ITEM_SIZE);
        }
        // draw helmet
        var xx = x + WIDTH + 2*SPACING;
        draw.rect(Color.white, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var helmet = this.player.items.getEquipment(EquipmentSlot.HELMET);
        if (helmet != null) {
            draw.image(helmet.definition.texture, xx, y+SPACING*5+4*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(Color.white, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var chestplate = this.player.items.getEquipment(EquipmentSlot.CHESTPLATE);
        if (chestplate != null) {
            draw.image(chestplate.definition.texture, xx, y+SPACING*4+3*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(Color.white, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var ring = this.player.items.getEquipment(EquipmentSlot.RING);
        if (ring != null) {
            draw.image(ring.definition.texture, xx, y+SPACING*3+2*ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(Color.white, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        var leggings = this.player.items.getEquipment(EquipmentSlot.LEGGINGS);
        if (leggings != null) {
            draw.image(leggings.definition.texture, xx, y+SPACING*2+ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
        }
        draw.rect(Color.white, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
        var boots = this.player.items.getEquipment(EquipmentSlot.BOOTS);
        if (boots != null) {
            draw.image(boots.definition.texture, xx, y+SPACING, ITEM_SIZE, ITEM_SIZE);
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
        float h  = -HEIGHT/2;
        float base = h+SPACING;
        float stride = ITEM_SIZE+SPACING;
        float row = (r-base)/stride;
        if (row < 0 || row >= NUM_ROWS) return -1;
        return (int) row;
    }
    public void detectClick(Screen screen, float x, float y) {
        if (isInBounds(screen, x, y)) {
            if (System.currentTimeMillis()-opened < 500) return;
            if (y > HEIGHT/2-.1f && x > (WIDTH+EQUIPMENT_PANEL_WIDTH)/2-.1f) {
                GUI.scheduleClear(0);
                GUI.toggle(62);
            }
            int slot = getX(x);
            if (slot == -1) {
                return;
            }
            int s2 = getY(screen, y);
            if (s2 == -1) {
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
        Item item;
        if ((item = player.items.getEquipment(slot)) == null)
            return;
        ResourceGiver[] givers;
        if ((givers = DeconstructorRegistry.resources.get(item.definition.id)) == null)
            return;
        player.items.setEquipment(slot, null);
        for (var giver : givers) giver.grant(new FullItemStorage(player.items, player.inventory, player.currency));
    }
    void switchWithItem(int index) {
        Item item;
        if ((item = player.items.get(index)) == null)
            return;
        ResourceGiver[] givers;
        if ((givers = DeconstructorRegistry.resources.get(item.definition.id)) == null)
            return;
        player.items.set(index, null);
        for (var giver : givers) giver.grant(new FullItemStorage(player.items, player.inventory, player.currency));
    }
}
