package main;

import combat.equipment.EquipmentSlot;
import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.input.Console;
import engine.core.input.EventManager;
import engine.core.input.Input;
import engine.core.text.Font;
import engine.saving.TokenizedSaveFile;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedDouble;
import org.lwjgl.glfw.GLFW;
import saving.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenu {
    private static final ArrayList<String> filenames = new ArrayList<>();
    private static final int PROFILES_PER_PAGE = 5;
    private static int page = 0;

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
                    new SavedStorage(new SavedStorage.Entry[]{}), new SavedStatusEffect[]{}, 100, 100, new SavedSkillSet(new SavedSkill[]{
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
                    new SavedSkill(0, 0),
            })), new SavedHome(WorldRegistry.get("HOUSE")), SavedQuests.empty(), new SavedAttributeData(new String[0]), new SavedDictionary(new HashMap<>()), 0);
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
        var font = Main.font;
        var pageCount = Math.max(1, (filenames.size() + PROFILES_PER_PAGE - 1) / PROFILES_PER_PAGE);
        page = Math.max(0, Math.min(page, pageCount - 1));
        var start = page * PROFILES_PER_PAGE;
        var end = Math.min(start + PROFILES_PER_PAGE, filenames.size());
        for (int i = start; i < end; ++i) {
            String s = filenames.get(i);
            float yy = .6f - (i - start) * .3f;
            draw.rect(Color.blue, -.2f, yy, .4f, .2f);
            float size = Math.min(font.getSizeForHeight(.2f), font.getSizeForWidth(s, .4f));
            draw.string(
                    s, Color.white,
                    -font.getWidthForSize(s, size)/2,
                    -font.getHeight(size)/2+yy+.1f,
                    font, size
            );
            if (clicked && x >= -.2f && x <= .2f && y >= yy && y <= yy+.2f) {
                var file = new TokenizedSaveFile();
                try {
                    file.readAndOverride(new FileReader("saves/"+s+".save"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Main.open(file, "saves/"+s+".save");
                Main.state = Main.State.ONGOING;
            }
        }
        drawPageControls(draw, font, x, y, clicked, pageCount);
    }

    private static void drawPageControls(DrawManager draw, Font font, float x, float y, boolean clicked, int pageCount) {
        var by = -.9f;
        var bw = .2f;
        var bh = .14f;
        var hasPrev = page > 0;
        var hasNext = page < pageCount - 1;
        var prevX = -.45f;
        var nextX = .25f;
        draw.rect(hasPrev ? Color.orange : Color.gray, prevX, by, bw, bh);
        draw.rect(hasNext ? Color.orange : Color.gray, nextX, by, bw, bh);
        drawCentered(draw, font, "Prev", Color.black, prevX + bw/2, by + bh/2, .09f);
        drawCentered(draw, font, "Next", Color.black, nextX + bw/2, by + bh/2, .09f);
        drawCentered(draw, font, "Page " + (page + 1) + " / " + pageCount, Color.white, 0f, by + bh/2, .09f);
        if (clicked) {
            if (hasPrev && x >= prevX && x <= prevX + bw && y >= by && y <= by + bh) page--;
            if (hasNext && x >= nextX && x <= nextX + bw && y >= by && y <= by + bh) page++;
        }
    }

    private static void drawCentered(DrawManager draw, Font font, String text, Color color, float cx, float cy, float height) {
        var size = font.getSizeForHeight(height);
        draw.string(
                text, color,
                cx - font.getWidthForSize(text, size)/2,
                cy - font.getHeight(size)/2,
                font, size
        );
    }

    public static void initLoadWorldMenu() {
        page = 0;
        filenames.clear();
        try (var paths = Files.walk(Path.of("saves"))) {
            paths.filter(Files::isRegularFile)
                    .map(path -> path.toString().substring(6, path.toString().length() - 5))
                    .forEach(filenames::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
