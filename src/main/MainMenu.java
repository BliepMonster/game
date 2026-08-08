package main;

import combat.equipment.EquipmentSlot;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import engine.core.input.Input;
import engine.saving.TokenizedSaveFile;
import org.lwjgl.glfw.GLFW;
import saving.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class MainMenu {
    private static final ArrayList<String> filenames = new ArrayList<>();

    public static void tick(DrawManager draw) {
        Screen screen = draw.getScreen();
        draw.rect(Color.green, -.1f, -.05f, .2f, .1f);
        draw.rect(Color.blue, -.1f, .1f, .2f, .1f);
        float x = Input.getMouseX(screen);
        float y = Input.getMouseY(screen);
        if (x >= -.1f && x <= .1f
                && y >= -.05f && y <= .05f
                && Input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)
        ) {
            SavedGame game = new SavedGame(new SavedPlayer(new SavedCurrencyHolder(0),
                    new SavedItemInventory(new SavedItem[]{}, new SavedItem[]{}, new EquipmentSlot[]{}),
                    new SavedStorage(new SavedStorage.Entry[]{}), new SavedStatusEffect[]{}, 100, new SavedSkillSet(new SavedSkill[]{
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0)
            })), new SavedHome(WorldRegistry.get("HOUSE")));
            MainMenu.initLoadWorldMenu();
            Main.open(game, "saves/save"+filenames.size()+".save");
            Main.state = Main.State.ONGOING;
        }
        if (x >= -.1f && x <= .1f
                && y >= .1f && y <= .2f
                && Input.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)
        ) {
            initLoadWorldMenu();
            Main.state = Main.State.LOAD_WORLD;
        }
    }

    public static void tickLoadingWorldMenu(DrawManager draw, Console console) {
        var screen = draw.getScreen();
        var events = console.input.events.get();
        var clicked = false;
        for (var event : events) {
            if (event.type() == EventManager.MOUSE_DOWN && event.key() == GLFW.GLFW_MOUSE_BUTTON_1) {
                clicked = true;
                break;
            }
        }
        var x = Input.getMouseX(screen);
        var y = Input.getMouseY(screen);
        var max = Math.min(10, filenames.size());
        var font = Main.font;
        for (int i = 0; i < max; ++i) {
            String s = filenames.get(i);
            float yy = -1+i*.2f;
            draw.rect(Color.blue, -.2f, yy, .4f, .2f);
            float size = Math.min(font.getSizeForHeight(.2f), font.getSizeForWidth(s, .4f));
            draw.string(
                    s, Color.white,
                    -font.getWidthForSize(s, size)/2,
                    -font.getHeight(size)/2+yy+.1f,
                    font, size
            );
            if (clicked && x >= -.2f && x <= .2f && y >= yy && y <= yy+.2f) {
                System.out.println("clicked");
                var file = new TokenizedSaveFile();
                try {
                    file.readAndOverride(new FileReader(s+".save"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Main.open(file, s+".save");
                Main.state = Main.State.ONGOING;
            }
        }
    }

    public static void initLoadWorldMenu() {
        filenames.clear();
        try (var paths = Files.walk(Path.of("saves"))) {
            paths.filter(Files::isRegularFile)
                    .map(path -> path.toString().substring(0, path.toString().length() - 5))
                    .forEach(str -> filenames.add(str));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
