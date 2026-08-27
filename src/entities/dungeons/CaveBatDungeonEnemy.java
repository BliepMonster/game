package entities.dungeons;

import combat.LootTableRegistry;
import engine.hitboxes.Hitbox;
import engine.hitboxes.RectangularHitbox;
import entities.DamageType;
import entities.projectiles.BasicArrowProjectile;
import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public class CaveBatDungeonEnemy extends DungeonEnemy {
    private int lastShoot = 0;
    public CaveBatDungeonEnemy(Vector2f position) {
        super(
                position,
                300,
                25,
                LootTableRegistry.get("base:cave_bat")
        );
    }
    public void moveSelf(Hitbox world) {

    }
    public float transformHealthLoss(float h, DamageType t) {
        return h;
    }
    @Override
    public float getKnockbackResistance() {
        return 0.5f;
    }
    public void tickSelf(GameContext context) {
        var player = context.getPlayer();
        if (Game.getTickCount()-lastShoot > 40) {
            lastShoot = Game.getTickCount();
            context.spawn(new BasicArrowProjectile(this, new Vector2f(player.getPosition()).sub(position).normalize().mul(.5f), 25));
        }
    }
    public void render(Camera camera) {
        camera.addRenderJob(
                () -> camera.rect(Color.black, position.x, position.y, 1, 1),
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
