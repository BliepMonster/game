package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.dungeons.MeleeDungeonEnemy;
import main.Camera;
import main.Game;
import org.joml.Vector2f;
import stats.inventory.items.ItemDefinition;

import java.util.HashMap;

public class DumbZombieEnemy extends MeleeDungeonEnemy {
    public static final HashMap<String, Integer> l = new HashMap<>();
    static {
        l.put("Zombie Fragment", 1);
    }
    public static final LootTable TABLE = new LootTable(
            new LootPiece(
                    LootType.COMMON,
                    new LootReward(
                            5,
                            new ItemDefinition[0],
                            new HashMap<>()
                    )
            ),
            new LootPiece(
                    LootType.RARE,
                    new LootReward(
                            5,
                            new ItemDefinition[0],
                            l
                    )
            )
    );
    @SuppressWarnings("unused")
    public DumbZombieEnemy(Vector2f position) {
        super(
                position,
                100,
                10,
                TABLE,
                8,
                12,
                0,
                5,
                10,
                2
        );
    }
    public void render(Camera camera) {
        var color = Game.getTickCount() <= lastHurtTick+5 ? java.awt.Color.red : java.awt.Color.orange;
        camera.addRenderJob(() -> camera.rect(color, position.x, position.y, 1, 1), -position.y);
    }

    private final Hitbox hitbox = new RectangularHitbox(
            0,0,1,1,this
    );

    @Override
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
