package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.Positioned;
import main.RenderContext;
import main.GameContext;

public interface Entity extends Positioned {
    void move(Hitbox world);
    void tick(GameContext context);
    void render(RenderContext context, float x, float y, float tsize);
    boolean shouldDie(GameContext context);
    Hitbox getHitbox();
    Hitbox getWalkHitbox();
}
