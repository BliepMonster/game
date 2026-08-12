package entities.particles;

import main.GameContext;
import org.joml.Vector2f;

import java.awt.*;

public final class ChipBurst {
    private ChipBurst() {}

    public static void burst(GameContext context, Vector2f origin, Vector2f awayFrom,
                             int count, Color[] colors, float minSize, float maxSize,
                             int lifeTicks, float speed, float spreadRadians, float lift, float gravity) {
        for (int i = 0; i < count; i++) {
            float angle = (float) Math.atan2(awayFrom.y, awayFrom.x);
            angle += (float) (Math.random() * 2 - 1) * spreadRadians;
            Vector2f dir = new Vector2f((float) Math.cos(angle), (float) Math.sin(angle));
            float size = minSize + (float) Math.random() * (maxSize - minSize);
            Color color = colors[(int) (Math.random() * colors.length)];
            Vector2f pos = new Vector2f(
                    origin.x + (float) Math.random() * .1f - .05f,
                    origin.y + (float) Math.random() * .1f - .05f
            );
            float sp = speed * (0.8f + (float) Math.random() * 0.4f);
            context.spawn(new SmoothedRectParticle(pos, lifeTicks, size, size, color, dir, sp, lift, gravity));
        }
    }
}
