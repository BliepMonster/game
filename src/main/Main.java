package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import engine.core.text.Font;
import engine.gui.GUI;
import org.lwjgl.glfw.GLFW;
import stats.inventory.ui.InventoryPanel;
import stats.skills.ui.SkillPanel;

public class Main {
    public static Font font;
    public static void main(String[] _args) {
        final var screen = new Screen();
        final var console = new Console();
        screen.create(console, Screen.MAXIMIZED);

        final var draw = new DrawManager(screen);
        final var context = new RenderContext(draw);

        font = new Font("res/Unnamed");

        final var game = new Game();

        GUI.init();
        GUI.bindDrawManager(draw);

        var lastTick = (double) System.currentTimeMillis();
        final var tickRate = 1000d/Game.TICKS_PER_SECOND;
        var pressed = false;

        main:
        while (screen.update()) {
            final var events = console.input.events.get();
            for (var event : events) {
                if (event.type() == EventManager.KEY_DOWN && event.key() == GLFW.GLFW_KEY_ESCAPE) {
                    screen.destroy();
                    break main;
                } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_E) {
                    if (GUI.getAll(0).isEmpty())
                        GUI.add(new InventoryPanel(game.player.inventory, font), 0);
                    else {
                        var component = GUI.getAll(0).getFirst();
                        if (component instanceof InventoryPanel)
                            GUI.clear(0);
                    }
                } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_K) {
                    if (GUI.getAll(0).isEmpty()) {
                        GUI.add(new SkillPanel(game.player.skills, font), 0);
                    } else {
                        var component = GUI.getAll(0).getFirst();
                        if (component instanceof SkillPanel)
                            GUI.clear(0);
                    }
                } else if (event.type() == EventManager.MOUSE_DOWN && event.key() == GLFW.GLFW_MOUSE_BUTTON_1) {
                    pressed = true;
                }
            }
            final var framestamp = System.currentTimeMillis();

            if (framestamp - lastTick >= tickRate) {
                lastTick += tickRate;
                game.tick(pressed, screen, .1f);
                pressed = false;
            }

            game.render(context, .1f);

            GUI.manage(console);

            console.input.events.clear();
            screen.display();
        }
    }
}
