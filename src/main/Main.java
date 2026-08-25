package main;

import engine.abstraction.Keybinds;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.audio.AudioMaster;
import engine.core.input.Console;
import engine.core.input.Event;
import engine.core.input.EventManager;
import engine.core.input.Input;
import engine.core.text.Font;
import engine.gui.GUI;
import engine.jobs.JobSystem;
import engine.notifications.NotificationSystem;
import engine.saving.objects.SaveFile;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import quests.QuestRegistry;
import quests.ui.QuestPanel;
import saving.SavedGame;
import stats.inventory.items.ui.HotbarComponent;
import stats.inventory.items.ui.InventoryGridComponent;
import stats.inventory.resources.ui.InventoryPanel;
import stats.skills.ui.SkillPanel;
import stats.ui.CoinDisplayComponent;
import stats.ui.HealthBar;
import stats.ui.ManaBar;
import tiles.crafting.CraftingStationTilePart;
import tiles.crafting.CookingStationTilePart;
import tiles.npc.Currency;
import tiles.npc.JsonShopDefinitionLoader;
import tool.editor.EditorToolState;
import tool.editor.JSONSaver;
import tool.editor.TilePartLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static boolean DEBUG_MODE = false;
    public static final boolean ALLOW_DEBUG = true;
    public enum State {
        MAIN_MENU,
        LOAD_WORLD,
        ONGOING
    }
    public static State state = State.MAIN_MENU;
    public static Font font;
    public static Object[] fund = new Object[1000000];

    static String filename;

    public static final float TILE_SIZE = .08f;

    static Game game;
    static boolean pressed, deadLastFrame;

    static EditorToolState mode = EditorToolState.O1;
    static double lastTick;
    static DrawManager mapd;

    public static void open(SaveFile file, String filename) {
        JobSystem.clear();
        game = new Game(mapd, TILE_SIZE, file);
        Main.filename = filename;

        GUI.clear();


        GUI.add(new HotbarComponent(game.player), 62);
        GUI.add(new CoinDisplayComponent(game.player, font, Currency.COINS), 61);

        GUI.add(new HealthBar(game.player), 62);
        GUI.add(new ManaBar(game.player), 62);
    }

    public static void open(SavedGame gamme, String filename) {
        JobSystem.clear();
        game = new Game(mapd, TILE_SIZE, gamme);
        Main.filename = filename;

        GUI.clear();


        GUI.add(new HotbarComponent(game.player), 62);
        GUI.add(new CoinDisplayComponent(game.player, font, Currency.COINS), 61);

        GUI.add(new HealthBar(game.player), 62);
        GUI.add(new ManaBar(game.player), 62);
    }

    public static boolean tick(ArrayList<Event> events, Screen screen, Console console, double tickRate) throws IOException {
        for (var event : events) {
            if (event.type() == EventManager.KEY_DOWN && event.key() == GLFW.GLFW_KEY_ESCAPE) {
                screen.destroy();
                return false;
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
            } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_J) {
                if (GUI.getAll(0).isEmpty()) {
                    GUI.add(new QuestPanel(game.getQuests(), game, Main.font, 0), 0);
                    GUI.toggle(62);
                } else {
                    var component = GUI.getAll(0).getFirst();
                    if (component instanceof QuestPanel) {
                        GUI.clear(0);
                        GUI.toggle(62);
                    }
                }
            } else if (event.type() == EventManager.KEY_UP && event.key() == GLFW.GLFW_KEY_TAB) {
                if (GUI.getAll(0).isEmpty()) {
                    GUI.add(new InventoryGridComponent(game), 0);
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
            } else if (event.type() == EventManager.KEY_DOWN && event.key() == GLFW.GLFW_KEY_F3 && ALLOW_DEBUG) DEBUG_MODE = !DEBUG_MODE;
        }

        Keybinds.manage(console);

        long framestamp = System.currentTimeMillis();

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
                JSONSaver.save(game.world, game.world.map).write(new FileWriter(game.world.address));
            if (Input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
                game.world.apply(mode, Input.getMouseX(screen), Input.getMouseY(screen), game, Main.TILE_SIZE);
        } while (framestamp - lastTick >= tickRate) {
            lastTick = framestamp;
            if (!game.player.isDead()) {
                if (deadLastFrame) {
                    game.player.respawn(game);
                    deadLastFrame = false;
                }
                game.tick(pressed, screen, TILE_SIZE, !b);
                double scroll = Input.getScrollY();
                if (scroll != 0) {
                    int slot = game.player.getSelectedHotbarSlot();
                    slot = (slot - (int) Math.signum(scroll) + 9) % 9;
                    game.player.selectItem(slot);
                }
            } else {
                deadLastFrame = true;
                Game.ticks++;
            }
            pressed = false;
            Input.clearScroll();
        }
        return true;

        
    }
    public static final int CURSOR_W = 32, CURSOR_H = 38;
    static void main() throws IOException {
        final var screen = new Screen();
        BufferedImage buffer = ImageIO.read(new File("res/textures/icons/cursor.png"));
        BufferedImage scaled = new BufferedImage(CURSOR_H, CURSOR_H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(buffer, 0, 0, CURSOR_W, CURSOR_W, null);
        g.dispose();
        final var console = new Console();
        screen.create(console, Screen.MAXIMIZED);
        AudioMaster.init();
        screen.setCursor(scaled, new Vector2i());
        StaticItemLoader.initAll();
        JsonShopDefinitionLoader.registerAll();
        QuestRegistry.registerAll();

        final var draw = new DrawManager(screen);
        mapd = new DrawManager(screen);

        font = new Font("res/Unnamed");

        TilePartLoader.font = font;

        GUI.init();
        GUI.bindDrawManager(draw);

        Main.lastTick = (double) System.currentTimeMillis();
        final var tickRate = 1000d/Game.TICKS_PER_SECOND;

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

        CraftingStationTilePart.setDefaultFont(font);
        CookingStationTilePart.setDefaultFont(font);

        while (screen.update()) {
            try {
                final var events = console.input.events.get();

                if (state == State.ONGOING) {
                    AudioMaster.setListenerData(game.player.getPosition());
                    if (!tick(events, screen, console, tickRate)) break;
                } else if (state == State.MAIN_MENU) MainMenu.tick(draw);
                else if (state == State.LOAD_WORLD) MainMenu.tickLoadingWorldMenu(draw, console);

                NotificationSystem.tick();

                console.input.events.clear();
                screen.display();
            } catch (Throwable t) {
                fund = null;
                System.gc();
                t.printStackTrace();
                if (DEBUG_MODE)
                    JSONSaver.save(game.world, game.world.map).write(new FileWriter(game.world.address));
                game.save().save().writeToFile(new FileWriter(Main.filename.substring(0, Main.filename.length()-5)+"_crash.save"));
                return;
            }
        }
        if (DEBUG_MODE)
            JSONSaver.save(game.world, game.world.map).write(new FileWriter(game.world.address));
        game.save().save().writeToFile(new FileWriter(Main.filename));
        AudioMaster.cleanup();
    }
}
