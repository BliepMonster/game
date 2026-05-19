package engine.gui;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.*;
import engine.gui.components.Component;

import java.util.ArrayList;

public class GUI {
    protected static DrawManager draw;
    protected static ArrayList<Component>[] components = (ArrayList<Component>[]) new ArrayList[64];
    protected static boolean[] enabled = new boolean[components.length];
    private static boolean[] scheduledClear = new boolean[components.length];
    public static void init() {
        for (int i = 0; i < components.length; i++) {
            components[i] = new ArrayList<>();
            enabled[i] = true;
        }
    }
    public static void scheduleClear(int i) {
        scheduledClear[i] = true;
    }
    public static void clearScheduled() {
        for (int i = 0; i < scheduledClear.length; i++) {
            if (!scheduledClear[i])
                continue;
            clear(i);
            scheduledClear[i] = false;
        }
    }

    public static DrawManager getBoundDrawManager() {
        return draw;
    }

    public static void bindDrawManager(DrawManager draw) {
        GUI.draw = draw;
    }

    public static void add(Component component, int location) {
        components[location].add(component);
    }
    public static void clear(int location) {
        components[location].clear();
    }
    public static void manage(Console console) {
        boolean pressed = false;
        for (Event event : console.input.events.get()) {
            if (event.type() == EventManager.MOUSE_UP) {
                pressed = true;
                break;
            }
        }
        Screen screen = draw.getScreen();
        for (int i = 0; i < components.length; i++) {
            if (!enabled[i])
                continue;
            ArrayList<Component> list = components[i];
            if (list.isEmpty())
                continue;
            for (Component component : list) {
                component.manage(draw, pressed);
            }
        }
        clearScheduled();
    }
    public static void clear() {
        for (int i = 0; i < components.length; i++) {
            clear(i);
        }
    }

    public static void toggle(int i) {
        enabled[i] = !enabled[i];
    }

    public static void toggle(int... i) {
        for (int t : i) {
            toggle(t);
        }
    }

    public static void disable(int i) {
        enabled[i] = false;
    }

    public static void disable(int... i) {
        for (int t : i) {
            disable(t);
        }
    }

    public static void enable(int i) {
        enabled[i] = true;
    }

    public static void enable(int... i) {
        for (int t : i) {
            enable(i);
        }
    }

    public static void toggleAll() {
        for (int i = 0; i < components.length; i++) {
            toggle(i);
        }
    }

    public static void clear(int... positions) {
        for (int i : positions) {
            clear(i);
        }
    }

    private GUI() {}
    public static ArrayList<Component> getAll(int i) {
        return components[i];
    }
    public static ArrayList<Component>[] getAll() {
        return components;
    }
    public static boolean isActive(int i) {
        return enabled[i];
    }
}
