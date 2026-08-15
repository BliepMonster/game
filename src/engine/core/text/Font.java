package engine.core.text;

import engine.core.Texture;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Font {
    protected Texture bitmap;
    protected Map<Character, Glyph> glyphs;
    protected int lineHeight = 70;
    protected int base = 54;
    protected float averageXAdvance = 28;
    protected float averageWidth = 22;
    public Font(String file) {
        bitmap = new Texture(file+".png");
        glyphs = load(file+".txt");
    }
    public HashMap<Character, Glyph> load(String file) {
        Pattern pattern = Pattern.compile("char id=(-?\\d+) x=(-?\\d+) y=(-?\\d+) width=(-?\\d+) height=(-?\\d+) xoffset=(-?\\d+) yoffset=(-?\\d+) xadvance=(-?\\d+) page=(-?\\d+) chnl=(-?\\d+)");
        Pattern commonPattern = Pattern.compile("common lineHeight=(\\d+) base=(\\d+)");
        HashMap<Character,Glyph> map = new HashMap<>();
        float totalAdvance = 0;
        float totalWidth = 0;
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher common = commonPattern.matcher(line);
                if (common.find()) {
                    lineHeight = Integer.parseInt(common.group(1));
                    base = Integer.parseInt(common.group(2));
                    continue;
                }
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    int charID = Integer.parseInt(matcher.group(1));
                    Glyph glyph = getGlyph(matcher);
                    map.put((char) charID, glyph);
                    totalAdvance += glyph.getXAdvance();
                    totalWidth += glyph.getWidth();
                    count++;
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        if (count > 0) {
            averageXAdvance = totalAdvance / count;
            averageWidth = totalWidth / count;
        }
        return map;
    }

    private static Glyph getGlyph(Matcher matcher) {
        int x = Integer.parseInt(matcher.group(2));
        int y = Integer.parseInt(matcher.group(3));
        int width = Integer.parseInt(matcher.group(4));
        int height = Integer.parseInt(matcher.group(5));
        int xOffset = Integer.parseInt(matcher.group(6));
        int yOffset = Integer.parseInt(matcher.group(7));
        int xAdvance = Integer.parseInt(matcher.group(8));
        return new Glyph(x,y,width,height,xOffset,yOffset,xAdvance);
    }
    public Glyph get(char c) {
        return glyphs.get(c);
    }
    public Texture getBitmap() {
        return bitmap;
    }
    public float getSizeForWidth(String s, float width) {
        float xA = getXAdvance(s);
        return width*100/xA;
    }
    public float getSizeForHeight(float height) {
        return height*100f/base;
    }
    public float getWidthForSize(String s, float size) {
        float xA = getXAdvance(s);
        return size/100*xA;
    }
    public int getLineHeight() {
        return lineHeight;
    }
    public int getBase() {
        return base;
    }
    public float getAverageXAdvance() {
        return averageXAdvance;
    }
    public float getAverageWidth() {
        return averageWidth;
    }
    public float getXAdvance(String s) {
        float xA = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') continue;
            xA += get(s.charAt(i)).getXAdvance();
        }
        return xA;
    }
    public float getHeight(float size) {
        return size*base/100f;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Font)) return false;
        if (!super.equals(other)) return false;
        var otherCast = (Font) other;
        if (!Objects.equals(this.bitmap, otherCast.bitmap)) return false;
        if (!Objects.equals(this.glyphs, otherCast.glyphs)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(bitmap);
        result = 31 * result + Objects.hashCode(glyphs);
        return result;
    }

    @Override
    public String toString() {
        return "Font[" + "bitmap=" + bitmap + ", " + "glyphs=" + glyphs + "]";
    }
}
