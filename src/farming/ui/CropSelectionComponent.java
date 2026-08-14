package farming.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import farming.CropType;
import main.Main;

import java.awt.*;
import java.util.Collection;

public class CropSelectionComponent extends Component {
    public static final Collection<CropType> crops = CropType.getAll();
    public final FarmingPotComponent owner;
    public final CropType[] allowed;
    private int from = 0;
    public CropSelectionComponent(FarmingPotComponent owner) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(1.6f));
        this.owner = owner;
        var player = owner.context.getPlayer();
        this.allowed = crops.stream().filter(crop -> player.inventory.contains(crop.seedType(), 1)).toArray(CropType[]::new);
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.gray, x, y, width, height);
        draw.rect(Color.green, x, y+height-.1f, .1f, .1f);
        float slotHeight = (height - .3f) / 5f;
        for (int i = 0; i < 5; i++) {
            int itemIndex = from + i;
            if (itemIndex >= allowed.length) break;

            CropType crop = allowed[itemIndex];
            float itemY = y + height - .2f - (i + 1) * slotHeight;

            // Slot background card
            draw.rect(new Color(60, 60, 60), x + .05f, itemY, width - .1f, slotHeight - .02f);

            // Crop Icon & Name
            if (crop.icon() != null) {
                draw.image(crop.icon(), x + .07f, itemY + .01f, slotHeight - .04f, slotHeight - .04f);
            }
            int count = owner.context.getPlayer().inventory.get(crop.seedType()).orElse(0);
            String text = crop.cropName() + " (" + count + ")";
            float fontSize = Main.font.getSizeForWidth(text, width - .3f);
            draw.string(text, Color.white, x + .2f, itemY + (slotHeight / 2f) - (Main.font.getHeight(fontSize) / 2f), Main.font, fontSize);
        }
        int page = from / 5 + 1;
        for (int i = -2; i < 3; ++i) {
            if (page + i <= 0 || (page+i-1)*5 >= allowed.length) continue;
            float f = x+(i+2)*width/5;
            draw.rect(i % 2 == 0 ? Color.green : Color.blue, f, y, width/5, height/10);
            String s = Integer.toString(page+i);
            float size = Main.font.getSizeForWidth(s, width/15);
            draw.string(s, Color.black, f+width/10-Main.font.getWidthForSize(s, size)/2, y+height/20-Main.font.getHeight(size)/2, Main.font, size);
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        if (y > .7f && x < -.3f) owner.isSelf = true;
        else if (y < -.64f) {
            int s = (int) ((x+.8f)*5/.8f-2);
            if (from+s*5 < 0 || from+s*5 >= allowed.length) return;
            from += s*5;
        } else {
            float height = getHeightConstraint().evaluate(screen);
            float relX = x - getXConstraint().evaluate(screen, getWidthConstraint().evaluate(screen));
            float relY = y - getYConstraint().evaluate(screen, height);
            float slotHeight = (height - .3f) / 5f;
            for (int i = 0; i < 5; i++) {
                int itemIndex = from + i;
                if (itemIndex >= allowed.length) break;

                float itemY = height - .2f - (i + 1) * slotHeight;
                if (relY >= itemY && relY <= itemY + slotHeight) {
                    CropType selectedCrop = allowed[itemIndex];

                    // Plant seed and deduct inventory
                    owner.owner.plant(selectedCrop);
                    owner.context.getPlayer().inventory.spend(selectedCrop.seedType(), 1);

                    // Return to main pot UI
                    owner.isSelf = true;
                    return;
                }
            }
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
