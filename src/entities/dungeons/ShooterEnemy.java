package entities.dungeons;

import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.DamageType;
import entities.LootTable;
import entities.projectiles.BasicArrowProjectile;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public class ShooterEnemy extends DungeonEnemy {
    public static final LootTable LOOT = new LootTable();
    private int lastShoot = 0;
    public ShooterEnemy(Vector2f position) {
        super(
                position,
                300,
                25,
                LOOT
        );
    }
    public void moveSelf(Hitbox world) {

    }
    public float transformHealthLoss(float h, DamageType t) {
        return h;
    }
    @Override
    public float getKnockbackResistance() {
        return 1;
    }
    public void tickSelf(GameContext context) {
        var player = context.getPlayer();
        if (Game.getTickCount()-lastShoot > 40) {
            lastShoot = Game.getTickCount();
            context.spawn(new BasicArrowProjectile(this, new Vector2f(player.getPosition()).sub(position).normalize().mul(.4f), 30));
        }
    }
    public void render(Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.white, getRenderX(), getRenderY(), 1, 1),
                -position.y
        );
    }
    private final Hitbox hitbox = new RectangularHitbox(0, 0, 1, 1, this);

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
