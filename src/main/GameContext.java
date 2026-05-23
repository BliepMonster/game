package main;

import entities.Entity;
import entities.Player;
import org.joml.Vector2f;

import java.util.List;

public interface GameContext {
    Player getPlayer();
    Vector2f getPlayerRespawnPosition();
    List<Entity> getEntities();
    void spawn(Entity entity);
}
