package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.Positioned;
import main.RenderContext;

public interface Entity extends Positioned {
    void move(Hitbox world);
    void tick();
    void render(RenderContext context, float x, float y, float tsize);
    boolean shouldDie();
    Hitbox getHitbox();
    Hitbox getWalkHitbox();
}
