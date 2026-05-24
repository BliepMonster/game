package tiles;

import entities.DamageType;
import main.Camera;
import main.Game;
import main.GameContext;

import java.awt.*;

public class MagmaTile extends Tile {
    private static int lastDamaged = 0;
    public boolean isSelfStandable() {
        return true;
    }
    public void onSelfStand(GameContext context) {
        var player = context.getPlayer();
        if (Game.getTickCount() - lastDamaged > Game.TICKS_PER_SECOND/2) {
            player.damage(5, DamageType.FIRE, context);
            lastDamaged = Game.getTickCount();
        }
    }
    public void renderSelf(Camera camera, int x, int y) {
        camera.rect(Color.ORANGE, x, y, 1, 1);
    }
}
