package tiles;

import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;

public class DualTile extends Tile {
    public final Tile a, b;
    public final String attrib;
    private GameContext context;
    public DualTile(Tile a, Tile b, String attrib, GameContext context) {
        this.a = a;
        this.b = b;
        this.attrib = attrib;
        this.context = context;
    }

    @Override
    public void onClick(GameContext context) {
        if (context.attributes().contains(attrib)) a.onClick(context);
        else b.onClick(context);
        this.context = context;
    }

    @Override
    public boolean isStandable() {
        if (context == null) return false;
        return context.attributes().contains(attrib) ? a.isStandable() : b.isStandable();
    }
    public boolean isSelfStandable() {
        return true; // unused
    }

    @Override
    public void onStand(GameContext context) {
        if (context.attributes().contains(attrib)) a.onStand(context);
        else b.onStand(context);
        this.context = context;
    }

    @Override
    public void onSelfStand(GameContext context) {
        // unused
    }
    public TilePart getPart() {
        return context.attributes().contains(attrib) ? a.getPart() : b.getPart();
    }

    @Override
    public void render(Camera camera, int x, int y) {
        if (context == null) {
            System.out.println("Context is null!");
            return;
        }
        if (context.attributes().contains(attrib)) a.render(camera, x, y);
        else b.render(camera, x, y);
    }
    public void renderSelf(Camera c, int x, int y) {}

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString("dual"));
        object.values.put("a", a.save());
        object.values.put("b", b.save());
        object.values.put("attrib", new JsonString(attrib));
        return object;
    }
}
