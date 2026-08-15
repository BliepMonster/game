package stats.inventory.items.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.graphics.Mesh;
import engine.core.graphics.TextMesh;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.ToolTipConstraintX;
import engine.gui.constraints.position.ToolTipConstraintY;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.Main;
import stats.inventory.items.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemTooltipComponent extends Component {
    public final Mesh text;
    public final Item item;
    public static final float FSIZE = .1f;
    public ItemTooltipComponent(Item item) {
        String title = item.definition.name;
        String desc = item.definition.description;
        List<String> descs = new ArrayList<String>();
        int index = 0;
        while (index < desc.length()) {
            descs.add(desc.substring(index, Math.min(index + 32, desc.length())));
            index += 32;
        }
        float w;
        if (descs.size() == 1) {
            w = Math.max(Main.font.getWidthForSize(title, FSIZE), Main.font.getWidthForSize(descs.getFirst(), FSIZE));
        } else {
            w = Main.font.getWidthForSize(title, FSIZE);
            for (var description : descs) {
                w = Math.max(w, Main.font.getWidthForSize(description, FSIZE));
            }
        }

        super(new ToolTipConstraintX(32f / GUI.getBoundDrawManager().getScreen().getHeight()),
                new ToolTipConstraintY(32f / GUI.getBoundDrawManager().getScreen().getHeight()),
                new RelativeSizeConstraint(w), new RelativeSizeConstraint(Main.font.getHeight(FSIZE) * (descs.size() + (descs.isEmpty() ? 1 : 3))));
        this.item = item;
        StringBuilder t = new StringBuilder();
        t.append(title).append(descs.isEmpty() ? "" : "\n");
        System.out.println(descs.size());
        for (var d : descs) {
            t.append("\n").append(d);
        }
        this.text = new TextMesh(t.toString(), Main.font, FSIZE, 200).create();
    }
    public static final Color BG = new Color(0, 0, 0, 230);

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(BG, x, y, width, height);
        draw.renderAsText((TextMesh) text, Main.font, Color.white, x, y+height-Main.font.getHeight(FSIZE));
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {

    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
