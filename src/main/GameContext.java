package main;

import entities.Entity;
import entities.Player;
import org.joml.Vector2f;
import quests.QuestTracker;
import tiles.Tile;

import java.util.List;

public interface GameContext {
    Player getPlayer();
    QuestTracker getQuests();
    Vector2f getPlayerRespawnPosition();
    List<Entity> getEntities();
    void spawn(Entity entity);
    Tile getTile(int x, int y);
    void movePlayer(World world, Vector2f position);
    World getWorld();
    AttributeData attributes();
}
