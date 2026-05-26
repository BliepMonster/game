package main;

import engine.abstraction.Keybinds;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import engine.core.input.Input;
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
import stats.ui.CoinDisplayComponent;
import stats.ui.HealthBar;
import tiles.CraftingStationTilePart;
import tiles.npc.Currency;
import tool.editor.EditorToolState;
import tool.editor.JSONSaver;
import tool.editor.TilePartLoader;

import java.io.FileWriter;
import java.io.IOException;

import static main.StaticItemLoader.*;

public class Main {
    public static final boolean DEBUG_MODE = true;
    public static Font font;
    public static void main(String[] _args) throws IOException {
        final var screen = new Screen();
        final var console = new Console();
        screen.create(console, Screen.MAXIMIZED);
        StaticItemLoader.initAll();

        final var draw = new DrawManager(screen);

        font = new Font("res/Unnamed");

        TilePartLoader.font = font;

        final var game = new Game(draw, .1f);

        GUI.init();
        GUI.bindDrawManager(draw);

        GUI.add(new HotbarComponent(game.player), 62);
        GUI.add(new CoinDisplayComponent(game.player, font, Currency.COINS), 61);

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

        var mode = EditorToolState.O1;


        CraftingStationTilePart.setDefaultFont(font);

        game.player.items.add(new Item(ItemDefinition.get(BREAD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(BREAD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(BREAD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(DAGGER_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(SWORD_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(CLUB_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(RUSTY_AXE_ITEM_ID)));
        game.player.items.add(new Item(ItemDefinition.get(RUSTY_PICKAXE_ITEM_ID)));

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

            game.render();

            boolean b = GUI.manage(console);

            if (DEBUG_MODE) {
                if (Input.isKeyDown(GLFW.GLFW_KEY_1)) mode = EditorToolState.O1;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_2)) mode = EditorToolState.O2;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_3)) mode = EditorToolState.O3;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_4)) mode = EditorToolState.O4;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_5)) mode = EditorToolState.O5;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_6)) mode = EditorToolState.O6;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_7)) mode = EditorToolState.O7;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_8)) mode = EditorToolState.O8;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_9)) mode = EditorToolState.O9;
                else if (Input.isKeyDown(GLFW.GLFW_KEY_S))
                    JSONSaver.save(game.world.map).write(new FileWriter(game.world.address));
                if (Input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1))
                    game.world.apply(mode, Input.getMouseX(screen), Input.getMouseY(screen), game, .1f);
            } if (framestamp - lastTick >= tickRate) {
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
        if (DEBUG_MODE)
            JSONSaver.save(game.world.map).write(new FileWriter(game.world.address));
    }
}
