package main;

import combat.melee.MeleeWeaponItem;
import engine.abstraction.Keybinds;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.notifications.NotificationSystem;
import org.lwjgl.glfw.GLFW;
import stats.inventory.items.Item;
import stats.inventory.items.ItemDefinition;
import stats.inventory.items.ui.HotbarComponent;
import stats.inventory.items.ui.InventoryGridComponent;
import stats.inventory.resources.ui.InventoryPanel;
import stats.skills.ui.SkillPanel;
import stats.ui.HealthBar;

import static main.StaticItemLoader.*;

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

        GUI.add(new HotbarComponent(game.player), 62);

        var lastTick = (double) System.currentTimeMillis();
        final var tickRate = 1000d/Game.TICKS_PER_SECOND;
        var pressed = false;

        NotificationSystem.font = font;

        Keybinds.addKeybind(GLFW.GLFW_KEY_1, () -> game.player.selectItem(0));
        Keybinds.addKeybind(GLFW.GLFW_KEY_2, () -> game.player.selectItem(1));
        Keybinds.addKeybind(GLFW.GLFW_KEY_3, () -> game.player.selectItem(2));
        Keybinds.addKeybind(GLFW.GLFW_KEY_4, () -> game.player.selectItem(3));
        Keybinds.addKeybind(GLFW.GLFW_KEY_5, () -> game.player.selectItem(4));
        Keybinds.addKeybind(GLFW.GLFW_KEY_6, () -> game.player.selectItem(5));
        Keybinds.addKeybind(GLFW.GLFW_KEY_7, () -> game.player.selectItem(6));
        Keybinds.addKeybind(GLFW.GLFW_KEY_8, () -> game.player.selectItem(7));
        Keybinds.addKeybind(GLFW.GLFW_KEY_9, () -> game.player.selectItem(8));


        StaticItemLoader.initAll();

        game.player.items.add(new Item(ItemDefinition.get(StaticItemLoader.BREAD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(StaticItemLoader.BREAD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(StaticItemLoader.BREAD_ITEM_ID)));
        game.player.items.add(new MeleeWeaponItem(DAGGER_ITEM_DEF));
        game.player.items.add(new MeleeWeaponItem(SWORD_ITEM_DEF));
        game.player.items.add(new MeleeWeaponItem(CLUB_ITEM_DEF));

        GUI.add(new HealthBar(game.player), 62);

        boolean deadLastFrame = false;

        main:
        while (screen.update()) {
            final var events = console.input.events.get();
            for (var event : events) {
                if (event.type() == EventManager.KEY_DOWN && event.key() == GLFW.GLFW_KEY_ESCAPE) {
                    screen.destroy();
                    break main;
                } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_E) {
                    if (GUI.getAll(0).isEmpty()) {
                        GUI.add(new InventoryPanel(game.player.inventory, font), 0);
                        GUI.toggle(62);
                    }
                    else {
                        var component = GUI.getAll(0).getFirst();
                        if (component instanceof InventoryPanel) {
                            GUI.clear(0);
                            GUI.toggle(62);
                        }
                    }
                } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_K) {
                    if (GUI.getAll(0).isEmpty()) {
                        GUI.add(new SkillPanel(game.player.skills, font), 0);
                        GUI.toggle(62);
                    } else {
                        var component = GUI.getAll(0).getFirst();
                        if (component instanceof SkillPanel) {
                            GUI.clear(0);
                            GUI.toggle(62);
                        }
                    }
                } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_TAB) {
                    if (GUI.getAll(0).isEmpty()) {
                        GUI.add(new InventoryGridComponent(game.player), 0);
                        GUI.toggle(62);
                    } else {
                        var component = GUI.getAll(0).getFirst();
                        if (component instanceof InventoryGridComponent) {
                            GUI.clear(0);
                            GUI.toggle(62);
                        }
                    }
                } else if (event.type() == EventManager.MOUSE_DOWN && event.key() == GLFW.GLFW_MOUSE_BUTTON_1) {
                    pressed = true;
                }
            }

            Keybinds.manage(console);

            final var framestamp = System.currentTimeMillis();

            game.render(context, .1f);

            boolean b = GUI.manage(console);

            if (framestamp - lastTick >= tickRate) {
                lastTick += tickRate;
                if (!game.player.isDead()) {
                    if (deadLastFrame) {
                        game.player.respawn(game);
                        deadLastFrame = false;
                    }
                    game.tick(pressed, screen, .1f, !b);
                } else {
                    deadLastFrame = true;
                    Game.ticks++;
                }
                pressed = false;
            }

            NotificationSystem.tick();

            console.input.events.clear();
            screen.display();
        }
    }
}
