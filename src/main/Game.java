package main;

import entities.Player;

public class Game {
    public static final int TICKS_PER_SECOND = 50;
    private final World world;
    final Player player = new Player();
    public Game() {
        world = new World();
    }
    public void tick() {
        world.tick();
        player.tick(world);
        player.move(world.getHitbox());
    }
    public void render(RenderContext context, float tsize) {
        world.render(context, player, tsize);
        player.render(context, -tsize/2, 0, tsize);
    }
}
