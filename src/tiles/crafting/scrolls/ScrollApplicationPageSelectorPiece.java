package tiles.crafting.scrolls;

import engine.core.DrawManager;
import engine.core.text.Font;
import main.Main;

import java.awt.*;

public class ScrollApplicationPageSelectorPiece {
    public void tick(float x, float y, DrawManager draw, int page, boolean selected) {
        draw.rect(selected ? Color.cyan : Color.green, x, y, .1f, .1f);
        Font font = Main.font;
        String s = Integer.toString(page);
        float size = Math.min(font.getSizeForHeight(.1f), font.getSizeForWidth(s, .1f));
        float xx = x+.05f-font.getWidthForSize(s, size)/2;
        draw.string(s, Color.black, xx, y, font, size);
    }
}
