package entities;

import engine.hitboxes.Hitbox;
import engine.hitboxes.Positioned;
import main.RenderContext;
import main.WorldContext;

public interface Entity extends Positioned {
    void move(Hitbox world);
    void tick(WorldContext context);
    void render(RenderContext context, float x, float y, float tsize);
    boolean shouldDie(WorldContext context);
    Hitbox getHitbox();
    Hitbox getWalkHitbox();
}
