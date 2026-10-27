package tiles.doors;

import engine.core.textures.Texture;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import main.Camera;
import main.GameContext;
import main.WorldRegistry;
import org.joml.Vector2f;
import tiles.TilePart;

import static tool.editor.TilePartLoader.ID_TRAPDOOR;

public class TrapdoorTilePart extends TilePart {
    private final String world;
    private final Vector2f spawnPosition;
    private final Texture texture;
    public TrapdoorTilePart(Vector2f tilePosition, String world, Vector2f spawnPosition, Texture texture) {
        super(tilePosition);
        if (world == null) throw new RuntimeException();
        this.world = world;
        this.spawnPosition = spawnPosition;
        this.texture = texture;
    }
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(texture, x, y, 1, 1),
                -y
        );
    }
    public boolean isStandable() {
        return false;
    }
    public void onStand(GameContext context) {
    }
    public void onClick(GameContext context) {
        context.movePlayer(WorldRegistry.get(world), spawnPosition);
    }
    public JsonValue save() {
        var map = new JsonObject();
        map.values.put("type", new JsonString(ID_TRAPDOOR));
        map.values.put("world_id", new JsonString(world));
        map.values.put("x", new JsonNumber(spawnPosition.x));
        map.values.put("y", new JsonNumber(spawnPosition.y));
        map.values.put("texture_id", new JsonString(texture.getAddress()));
        return map;
    }

    @Override
    public boolean canBreak() {
        return false;
    }
}
