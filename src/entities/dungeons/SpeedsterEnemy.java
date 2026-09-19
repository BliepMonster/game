package entities.dungeons;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.LootPiece;
import entities.LootReward;
import entities.LootTable;
import entities.LootType;
import main.Camera;
import main.Game;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;

import java.awt.*;
import java.util.HashMap;

public class SpeedsterEnemy extends MeleeDungeonEnemy {
    public static final LootTable TABLE = new LootTable(
            new LootPiece(
                    LootType.LEGENDARY,
                    new LootReward(
                            0,
                            new ItemDefinition[]{ItemDefinition.get("base:armor:speed_boots")},
                            new HashMap<>()
                    )
            )
    );
    public SpeedsterEnemy(Vector2f location) {
        super(
                location,
                450,
                40,
                TABLE,
                10,
                18,
                0,
                10,
                20,
                6
        );
    }
    public final Hitbox hitbox = new RectangularHitbox(
            0,
            0,
            1,
            1,
            this
    );
    public void render(Camera camera) {
        camera.addRenderJob(() -> camera.rect(Game.getTickCount()-lastHurtTick < 5 ? Color.red : Color.green, getRenderX(), getRenderY(), getWidth(), getHeight()), -position.y);
    }
    public Hitbox getHitbox() {
        return hitbox;
    }
    public float getWidth() {
        return 1;
    }
    public float getHeight() {
        return 1;
    }
}
