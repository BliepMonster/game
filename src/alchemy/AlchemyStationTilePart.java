package alchemy;

import engine.core.textures.Texture;
import engine.gui.GUI;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import main.TextureIdMap;
import org.joml.Vector2f;
import tiles.TilePart;

import static tool.editor.TilePartLoader.ID_ALCHEMY_STATION;

public class AlchemyStationTilePart extends TilePart {
    public AlchemyCraftingRecipe recipe;
    public long end;
    public static final Texture TEXTURE = TextureIdMap.get("base:tiles:alchemy_station");

    public AlchemyStationTilePart(Vector2f position) {
        super(position);
    }
    public AlchemyStationTilePart(Vector2f position, AlchemyCraftingRecipe recipe, long end) {
        super(position);
        this.recipe = recipe;
        this.end = end;
    }

    public boolean isDone() {
        return recipe != null && System.currentTimeMillis() >= end;
    }

    @Override
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(() -> camera.image(TEXTURE, x, y, 1, 1), -y);
        if (recipe != null) {
            camera.addRenderJob(() -> camera.image(recipe.icon(), x+.25f, y+.25f, .5f, .5f), -y);
        }
    }
    @Override
    public boolean isStandable() {
        return false;
    }
    @Override
    public void onStand(GameContext context) {}
    @Override
    public void onClick(GameContext context) {
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new AlchemyStationPanel(this, context), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public boolean canBreak() {
        return recipe == null;
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString(ID_ALCHEMY_STATION));
        if (recipe != null) {
            object.values.put("recipe", new JsonString(recipe.id()));
            object.values.put("end", new JsonNumber(end));
        }
        return object;
    }
}
