package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.jobs.JobSystem;
import entities.Entity;
import entities.Player;
import org.joml.Vector2f;

import java.util.List;

public class Game implements GameContext {
    public static final int TICKS_PER_SECOND = 20;
    static int ticks = 0;
    public static int getTickCount() {
        return ticks;
    }
    private final World world;
    final Player player = new Player();
    private final Camera camera;
    public Player getPlayer() {
        return player;
    }
    public Game(DrawManager draw, float tsize) {
        world = World.home();
        camera = new Camera(player, tsize, draw);
    }
    public void tick(boolean pressed, Screen screen, float tsize, boolean canClick) {
        ++ticks;
        if (player.shouldDie(this) && !player.isDead()) {
            player.die(this);
        }
        world.tick(this, pressed, screen, tsize, canClick);
        player.tick(this);
        player.move(world.getHitbox());

        JobSystem.tick(1f/Game.TICKS_PER_SECOND);
    }
    public void render() {
        world.render(camera);
        player.render(camera);
        camera.renderAllJobs();
    }

    @Override
    public Vector2f getPlayerRespawnPosition() {
        return new Vector2f(0, 0);
    }
    public List<Entity> getEntities() {
        return world.entities;
    }
    public void spawn(Entity entity) {
        world.spawn(entity);
    }
}
