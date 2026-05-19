package engine.abstraction;

import engine.core.input.Console;
import engine.core.input.Event;
import org.lwjgl.glfw.GLFW;

import static engine.core.input.EventManager.KEY_DOWN;

public final class Keybinds {
    private static final Runnable[] binds = new Runnable[GLFW.GLFW_KEY_LAST+1];
    private static final boolean[] enabled = new boolean[GLFW.GLFW_KEY_LAST+1];
    public static void addKeybind(int key, Runnable bind) {
        binds[key] = bind;
        enabled[key] = true;
    }
    public static void manage(Console console) {
        for (Event event : console.input.events.get()) {
            if (event.type() == KEY_DOWN && enabled[event.key()]) {
                binds[event.key()].run();
            }
        }
    }
    public static void remove(int key) {
        binds[key] = null;
        enabled[key] = false;
    }
    public static void enable(int key) {
        enabled[key] = true;
    }
    public static void disable(int key) {
        enabled[key] = false;
    }

    // do not instantiate
    private Keybinds() {}
}
