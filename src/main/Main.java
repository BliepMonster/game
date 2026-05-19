package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import org.lwjgl.glfw.GLFW;

public class Main {
    public static void main(String[] args) {
        final var screen = new Screen();
        final var console = new Console();
        screen.create(console, Screen.MAXIMIZED);

        final var draw = new DrawManager(screen);
        final var context = new RenderContext(draw);

        final var game = new Game();

        var lastTick = (double) System.currentTimeMillis();
        final var tickRate = 1000d/Game.TICKS_PER_SECOND;

        main:
        while (screen.update()) {
            final var events = console.input.events.get();
            for (var event : events) {
                if (event.type() == EventManager.KEY_DOWN && event.key() == GLFW.GLFW_KEY_ESCAPE) {
                    screen.destroy();
                    break main;
                }
            }
            final var framestamp = System.currentTimeMillis();

            if (framestamp - lastTick >= tickRate) {
                lastTick += tickRate;
                game.tick();
            }

            game.render(context, .1f);

            console.input.events.clear();
            screen.display();
        }
    }
}
