package entities;

import main.Camera;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;

public class DumbZombieEnemy extends DefaultMeleeEnemy {
    @SuppressWarnings("unused")
    public DumbZombieEnemy(Vector2f position) {
        super(
                position,
                100,
                5,
                12,
                10,
                10,
                1,
                1,
                10
        );
    }
    public void render(Camera camera) {
        var color = Game.getTickCount() <= getLastHurtTick()+5 ? java.awt.Color.red : java.awt.Color.orange;
        camera.addRenderJob(() -> camera.rect(color, position.x, position.y, 1, 1), -position.y);
    }
    public void giveLoot(GameContext context) {}
}
