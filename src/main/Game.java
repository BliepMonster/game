package main;

import engine.core.Screen;
import engine.jobs.JobSystem;
import entities.Player;

public class Game implements GameContext {
    public static final int TICKS_PER_SECOND = 50;
    private final World world;
    final Player player = new Player();
    public Player getPlayer() {
        return player;
    }
    public Game() {
        world = World.home(player);
    }
    public void tick(boolean pressed, Screen screen, float tsize) {
        world.tick(this, pressed, screen, tsize);
        player.tick(this);
        player.move(world.getHitbox());

        JobSystem.tick(1f/Game.TICKS_PER_SECOND);
    }
    public void render(RenderContext context, float tsize) {
        world.render(context, player, tsize);
        player.render(context, -tsize/2, 0, tsize);
    }
}
