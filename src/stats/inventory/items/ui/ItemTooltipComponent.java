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
import main.GameContext;
import main.Main;
import stats.StatHolder;
import stats.inventory.items.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemTooltipComponent extends Component {
    public final Mesh text;
    public final Item item;
    public static final float FSIZE = .1f;
    public ItemTooltipComponent(Item item, GameContext context) {
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

        var held = item.definition.getHeldStats_(item, context);
        String hs;
        ArrayList<String> cache = new ArrayList<>();
        if (held.isEmpty()) hs = "";
        else {
            var sb = new StringBuilder("\n\nWhen held:");
            cache.add("When held: ");
            for (int i = 0; i < StatHolder.Stat.values().length; i++) {
                float f = held.get(StatHolder.Stat.values()[i]);
                if (f == 0) continue;
                String s = ("\n")+(StatHolder.Stat.values()[i].name)+(": ")+(f);
                cache.add(s);
                sb.append(s);
            }
            hs = sb.toString();
        }
        for (String s : cache) {
            w = Math.max(w, Main.font.getWidthForSize(s, FSIZE));
        }
        int newlines = 0;
        for (char c : hs.toCharArray()) {
            if (c == '\n') newlines++;
        }
        var worn = item.definition.getWornStats_(item, context);
        String ws;
        cache = new ArrayList<>();
        if (worn.isEmpty()) ws = "";
        else {
            var sb = new StringBuilder("\n\nWhen worn:");
            cache.add("When worn: ");
            for (int i = 0; i < StatHolder.Stat.values().length; i++) {
                float f = worn.get(StatHolder.Stat.values()[i]);
                if (f == 0) continue;
                String s = ("\n")+(StatHolder.Stat.values()[i].name)+(": ")+(f);
                cache.add(s);
                sb.append(s);
            }
            ws = sb.toString();
        }
        for (String s : cache) {
            w = Math.max(w, Main.font.getWidthForSize(s, FSIZE));
        }
        int newlines2 = 0;
        for (char c : ws.toCharArray()) {
            if (c == '\n') newlines2++;
        }

        super(new ToolTipConstraintX(Main.CURSOR_W / (float) GUI.getBoundDrawManager().getScreen().getHeight()),
                new ToolTipConstraintY(Main.CURSOR_H / (float) GUI.getBoundDrawManager().getScreen().getHeight()),
                new RelativeSizeConstraint(w), new RelativeSizeConstraint(Main.font.getHeight(FSIZE) * (descs.size() + (descs.isEmpty() ? 1 : 3) + (newlines == 0 ? 0 : newlines+1) + (newlines2 == 0 ? 0 : newlines2+1))));
        this.item = item;
        StringBuilder t = new StringBuilder();
        t.append(title).append(descs.isEmpty() ? "" : "\n");
        for (var d : descs) {
            t.append("\n").append(d);
        }
        t.append(hs.isEmpty() ? "" : hs);
        t.append(ws.isEmpty() ? "" : ws);
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
