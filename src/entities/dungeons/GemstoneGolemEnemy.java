package entities.dungeons;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.LootPiece;
import entities.LootReward;
import entities.LootTable;
import entities.LootType;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;
import tiles.spawner.EnemySpawner;

import java.awt.*;
import java.util.HashMap;

public class GemstoneGolemEnemy extends MeleeDungeonEnemy {
    private final EnemySpawner spawner;
    public static final LootTable table;
    static {
        var map = new HashMap<String, Integer>();
        map.put("Ruby", 3);
        table = new LootTable(new LootPiece(LootType.COMMON, new LootReward(1, new ItemDefinition[]{}, map)));
    }
    public GemstoneGolemEnemy(Vector2f location, EnemySpawner spawner) {
        super(
                location,
                150,
                60,
                table,
                8,
                6,
                300,
                50,
                40,
                1
        );
        this.spawner = spawner;
    }
    public final Hitbox hitbox = new RectangularHitbox(0, 0, 1, 1, this);
    public void render(Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.cyan, getX(), getY(), 1, 1), -getY()
        );
    }

    @Override
    public void die(GameContext context) {
        super.die(context);
        spawner.onMobDeath();
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    @Override
    public float getWidth() {
        return 1;
    }

    @Override
    public float getHeight() {
        return 1;
    }
}
