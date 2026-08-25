package tiles.house;

import combat.equipment.EquipmentSlot;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Input;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import entities.Player;
import main.Main;
import stats.inventory.items.Item;

import java.awt.Color;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class WardrobePanel extends Component {
    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HELMET, EquipmentSlot.CHESTPLATE, EquipmentSlot.RING,
            EquipmentSlot.LEGGINGS, EquipmentSlot.BOOTS
    };
    private static final float SLOT_SIZE = .1f;
    private static final float SPACING = .03f;
    private static final float BUTTON_WIDTH = .25f;
    private static final float BUTTON_HEIGHT = .07f;
    private static final Color PANEL_BG = new Color(40, 40, 40);
    private static final Color SWAP_ON = new Color(80, 180, 80);
    private static final Color SWAP_OFF = new Color(120, 120, 120);

    private final WardrobeTilePart part;
    private final Player player;
    private boolean swapEmpty = true;
    private float panelX, panelY, panelW, panelH;

    public WardrobePanel(WardrobeTilePart part, Player player) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER,
                new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(1.2f));
        this.part = part;
        this.player = player;
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        Screen screen = draw.getScreen();
        float w = getWidthConstraint().evaluate(screen);
        float h = getHeightConstraint().evaluate(screen);
        panelX = getXConstraint().evaluate(screen, w);
        panelY = getYConstraint().evaluate(screen, h);
        panelW = w;
        panelH = h;
        super.manage(draw, pressed);
        if (Input.isKeyDown(GLFW_KEY_ESCAPE)) close();
    }

    @Override
    public void display(DrawManager draw, float x, float y, float w, float h) {
        draw.rect(PANEL_BG, x, y, w, h);
        draw.rect(Color.red, x + w - .1f, y + h - .1f, .1f, .1f);

        float titleSize = Math.min(Main.font.getSizeForWidth("Wardrobe", w * .5f), Main.font.getSizeForHeight(h * .08f));
        draw.string("Wardrobe", Color.white,
                x + w / 2 - Main.font.getWidthForSize("Wardrobe", titleSize) / 2,
                y + h - .15f, Main.font, titleSize);

        float leftX = x + SPACING * 2;
        float rightX = x + w / 2 + SPACING * 2;
        float startY = y + h - .3f;
        float fontSize = Math.min(Main.font.getSizeForWidth("Chestplate", SLOT_SIZE), Main.font.getSizeForHeight(SLOT_SIZE * .8f));

        for (int i = 0; i < 5; i++) {
            float slotY = startY - i * (SLOT_SIZE + SPACING);

            draw.rect(Color.gray, leftX, slotY, SLOT_SIZE, SLOT_SIZE);
            Item equipped = player.items.getEquipment(SLOTS[i]);
            if (equipped != null)
                draw.image(equipped.definition.texture, leftX, slotY, SLOT_SIZE, SLOT_SIZE);

            draw.string("->", Color.white, leftX + SLOT_SIZE + SPACING, slotY + SLOT_SIZE * .15f, Main.font, fontSize);

            draw.rect(Color.darkGray, rightX, slotY, SLOT_SIZE, SLOT_SIZE);
            Item wardrobeItem = part.items[i];
            if (wardrobeItem != null)
                draw.image(wardrobeItem.definition.texture, rightX, slotY, SLOT_SIZE, SLOT_SIZE);
        }

        float btnX = x + w / 2 - BUTTON_WIDTH / 2;
        float btnY = y + SPACING * 2;
        draw.rect(Color.orange, btnX, btnY, BUTTON_WIDTH, BUTTON_HEIGHT);
        float btnFontSize = Math.min(Main.font.getSizeForWidth("Swap Set", BUTTON_WIDTH - .02f), Main.font.getSizeForHeight(BUTTON_HEIGHT * .7f));
        draw.string("Swap Set", Color.black,
                btnX + BUTTON_WIDTH / 2 - Main.font.getWidthForSize("Swap Set", btnFontSize) / 2,
                btnY + BUTTON_HEIGHT * .15f, Main.font, btnFontSize);

        float togY = btnY + BUTTON_HEIGHT + SPACING;
        draw.rect(swapEmpty ? SWAP_ON : SWAP_OFF, btnX, togY, BUTTON_WIDTH, BUTTON_HEIGHT);
        String toggleText = swapEmpty ? "Include Empty: ON" : "Include Empty: OFF";
        float togFontSize = Math.min(Main.font.getSizeForWidth(toggleText, BUTTON_WIDTH - .02f), Main.font.getSizeForHeight(BUTTON_HEIGHT * .7f));
        draw.string(toggleText, Color.white,
                btnX + BUTTON_WIDTH / 2 - Main.font.getWidthForSize(toggleText, togFontSize) / 2,
                togY + BUTTON_HEIGHT * .15f, Main.font, togFontSize);
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;

        float closeX = panelX + panelW - .1f;
        float closeY = panelY + panelH - .1f;
        if (x >= closeX && x <= closeX + .1f && y >= closeY && y <= closeY + .1f) {
            close();
            return;
        }

        float btnX = panelX + panelW / 2 - BUTTON_WIDTH / 2;
        float btnY = panelY + SPACING * 2;
        if (x >= btnX && x <= btnX + BUTTON_WIDTH && y >= btnY && y <= btnY + BUTTON_HEIGHT) {
            doSwap();
            return;
        }

        float togY = btnY + BUTTON_HEIGHT + SPACING;
        if (x >= btnX && x <= btnX + BUTTON_WIDTH && y >= togY && y <= togY + BUTTON_HEIGHT) {
            swapEmpty = !swapEmpty;
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {}

    private void doSwap() {
        for (int i = 0; i < 5; i++) {
            if (!swapEmpty && part.items[i] == null) continue;
            Item temp = player.items.getEquipment(SLOTS[i]);
            player.items.setEquipment(SLOTS[i], part.items[i]);
            part.items[i] = temp;
        }
    }

    private void close() {
        GUI.scheduleClear(0);
        GUI.toggle(62);
    }
}
