package stats.inventory.resources.ui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.graphics.TextMesh;
import engine.gui.components.Component;
import engine.gui.constraints.position.EdgeAlignmentConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.Main;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DropTrackerComponent extends Component {
    public static class Drop {
        String item; int amount; long timer; TextMesh mesh;String rs;

        public Drop(String item, long timer, int amount) {
            this.item = item;
            this.timer = timer;
            this.amount = amount;
            this.rs = "+"+amount+" "+item;
            this.mesh = (TextMesh) new TextMesh(rs, Main.font, size, 10000).create();
        }
        public void update(Drop drop) {
            this.amount += drop.amount;
            this.timer = drop.timer;
            mesh.destroy();
            this.rs = "+"+amount+" "+item;
            mesh = (TextMesh) new TextMesh(rs, Main.font, size, 10000).create();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Drop drop)
                return this.item.equals(drop.item);
            return false;
        }
        public int hashCode() {
            return item.hashCode();
        }
    }

    HashMap<Drop, Drop> drops = new HashMap<>();
    public DropTrackerComponent() {
        super(new EdgeAlignmentConstraint(EdgeAlignmentConstraint.RIGHT), new EdgeAlignmentConstraint(EdgeAlignmentConstraint.BOTTOM), new RelativeSizeConstraint(.1f), new RelativeSizeConstraint(0));
    }
    public void add(Drop drop) {
        if (drops.containsKey(drop)) {
            var dr = drops.get(drop);
            dr.update(drop);
        } else {
            drops.put(drop, drop);
        }
    }
    public static final float size = .1f;
    public void display(DrawManager draw, float x, float y, float w, float h) {
        var fh = Main.font.getHeight(size);
        int counter = 0;
        Set<Drop> dd = new HashSet<>();
        for (var entry : drops.entrySet()) {
            var drop = entry.getKey();
            if (drop.timer < System.currentTimeMillis()-5000) {
                dd.add(drop);
                drop.mesh.destroy();
                continue;
            }
            var fw = Main.font.getWidthForSize(drop.rs, size);
            draw.renderAsText(drop.mesh, Main.font, Color.white, x+w-fw, y+counter*fh);
            counter++;
        }
        for (var drop : dd) {
            drops.remove(drop);
        }
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {

    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
