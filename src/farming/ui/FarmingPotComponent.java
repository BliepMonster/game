package farming.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.graphics.Mesh;
import engine.core.graphics.TextMesh;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import farming.GardenPotTilePart;
import main.GameContext;
import main.Main;

import java.awt.*;

public class FarmingPotComponent extends Component {
    public final GardenPotTilePart owner;
    public final GameContext context;
    boolean isSelf = true;
    Component state;
    public static final Mesh READY = new TextMesh("Harvest", Main.font, Main.font.getSizeForWidth("Harvest", .4f), 1000).create();
    public static final Mesh COMPOST = new TextMesh("Apply Compost", Main.font, Main.font.getSizeForWidth("Apply Compost", .4f), 1000).create();
    public static final Mesh PLANT = new TextMesh("Plant", Main.font, Main.font.getSizeForWidth("Plant", .4f), 1000).create();
    public FarmingPotComponent(GardenPotTilePart owner, GameContext context) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(1.2f), new RelativeSizeConstraint(1f));
        this.owner = owner;
        this.context = context;
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        if (isSelf) super.manage(draw, pressed);
        else state.manage(draw, pressed);
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.gray, x, y, width, height);
        draw.rect(Color.red, x+width-.1f, y+height-.1f, .1f, .1f);
        if (owner.crop != null) {
            draw.image(owner.crop.type().icon(), x, y, .8f, .8f);
            if (owner.canHarvest(context)) {
                draw.rect(Color.orange, .2f, y, .4f, .2f);
                draw.renderAsText((TextMesh) READY, Main.font, Color.black, .2f, y);
            }
        } else {
            draw.rect(Color.red, .2f, y+.2f, .4f, .2f);
            draw.renderAsText((TextMesh) COMPOST, Main.font, Color.black, .2f, y+.2f);
            draw.rect(Color.cyan, .2f, y+.4f, .4f, .2f);
            draw.renderAsText((TextMesh) PLANT, Main.font, Color.black, .2f, y+.4f);
        }
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (!isInBounds(screen, x, y)) return;
        if (x > .5f && y > .4f) close();
        else if (x > .2f && y < -.3f)
            this.owner.harvest(context); // takes care of issues
        else if (x > .2f && y < -.1f && owner.crop == null) {}
        else if (x > .2f && y < .1f && owner.crop == null){
            isSelf = false;
            state = new CropSelectionComponent(this);
        }
    }
    public void close() {
        GUI.toggle(62);
        GUI.scheduleClear(0);
    }
}
