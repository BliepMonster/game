package tiles.doors;

import engine.core.Texture;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import engine.notifications.NotificationSystem;
import main.Camera;
import main.GameContext;
import main.WorldRegistry;
import org.joml.Vector2f;
import tiles.TilePart;

import java.awt.*;

import static tool.editor.TilePartLoader.ID_DUNGEON_EXIT;

public class DungeonExitTilePart extends TilePart {
    private final String world;
    private final Vector2f spawnPosition;
    private final Texture texture;
    public DungeonExitTilePart(String world, Vector2f position, Texture texture) {
        if (world == null) throw new RuntimeException();
        this.world = world;
        this.spawnPosition = position;
        this.texture = texture;
    }
    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(
                () -> camera.image(texture, x, y, 1, 1),
                -y
        );
    }
    public boolean isStandable() {
        return true;
    }
    public void onStand(GameContext context) {
        if (!context.getEntities().isEmpty()) {
            NotificationSystem.add(
                    "You have not killed all enemies yet!",
                    Color.RED
            );
            return;
        }
        context.movePlayer(WorldRegistry.get(world), spawnPosition);
    }
    public void onClick(GameContext context) {

    }
    public JsonValue save() {
        var map = new JsonObject();
        map.values.put("type", new JsonString(ID_DUNGEON_EXIT));
        map.values.put("world_id", new JsonString(world));
        map.values.put("x", new JsonNumber(spawnPosition.x));
        map.values.put("y", new JsonNumber(spawnPosition.y));
        map.values.put("texture_id", new JsonString(texture.getAddress()));
        return map;
    }
}
