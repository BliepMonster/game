package engine.core.graphics;

import engine.core.Texture;
import engine.core.text.Font;
import engine.core.text.Glyph;

import java.util.ArrayList;

import static engine.core.DrawManager.*;

public class TextMesh extends Mesh {
    public TextMesh(String text, Font font, float size, float paragraphWidthLimit) {
        float xPosition = 0;
        float yPosition = 0;
        ArrayList<float[]> quads = new ArrayList<>();
        ArrayList<float[]> texQuads = new ArrayList<>();
        for (char c : text.toCharArray()) {
            if (c == '\n') {
                xPosition = 0;
                yPosition -= SIZE_PER_FONT_SIZE*size*NEW_LINE;
                continue;
            }
            float[] positions = new float[12];
            float[] texCoords;
            Glyph glyph;
            float glyphX;
            float glyphY;
            float absoluteWidth;
            float absoluteHeight;
            float width;
            float height;
            float xAdvance;
            float xOffset;
            float yOffset;
            float demoHeight = DEMO_HEIGHT*SIZE_PER_FONT_SIZE*size;
            if ((glyph = font.get(c)) != null) {
                glyphX = glyph.getX();
                glyphY = glyph.getY();
                absoluteWidth = glyph.getWidth();
                absoluteHeight = glyph.getHeight();
                width = absoluteWidth*SIZE_PER_FONT_SIZE*size;
                height = absoluteHeight*SIZE_PER_FONT_SIZE*size;
                xAdvance = glyph.getXAdvance()*SIZE_PER_FONT_SIZE*size;
                xOffset = glyph.getXOffset()*SIZE_PER_FONT_SIZE*size;
                yOffset = glyph.getYOffset()*SIZE_PER_FONT_SIZE*size;
            } else {
                glyphX = 1000;
                glyphY = 1000;
                absoluteHeight = 2;
                absoluteWidth = 2;
                width = DEMO_WIDTH*SIZE_PER_FONT_SIZE*size;
                height = demoHeight;
                xAdvance = DEMO_X_ADVANCE*SIZE_PER_FONT_SIZE*size;
                xOffset = 0;
                yOffset = 0;
            }
            Texture bitmap = font.getBitmap();
            float texHeight0 = glyphY/bitmap.getHeight();
            float texHeight1 = texHeight0+(absoluteHeight/bitmap.getHeight());
            float texWidth0 = glyphX/bitmap.getWidth();
            float texWidth1 = texWidth0+(absoluteWidth/bitmap.getWidth());
            positions[0] = xOffset + xPosition;
            positions[1]=demoHeight - yOffset + yPosition;
            positions[2] = width + xOffset + xPosition;
            positions[3]=demoHeight - yOffset + yPosition;
            positions[4] = width + xOffset + xPosition;
            positions[5]=demoHeight - yOffset - height + yPosition;
            positions[6] = xOffset + xPosition;
            positions[7]=demoHeight - yOffset - height + yPosition;
            positions[8] = xOffset + xPosition;
            positions[9]=demoHeight - yOffset + yPosition;
            positions[10] = width + xOffset + xPosition;
            positions[11]=demoHeight - yOffset - height + yPosition;
            texCoords = new float[] {
                    texWidth0, texHeight0,
                    texWidth1, texHeight0,
                    texWidth1, texHeight1,
                    texWidth0, texHeight1,
                    texWidth0, texHeight0,
                    texWidth1, texHeight1
            };
            xPosition += xAdvance;
            if (xPosition >= 100*paragraphWidthLimit) {
                xPosition = 0;
                yPosition -= SIZE_PER_FONT_SIZE*size*NEW_LINE;
            }
            quads.add(positions);
            texQuads.add(texCoords);
        }
        float[] positions = new float[12*quads.size()];
        for (int i = 0; i < quads.size(); i++) {
            float[] floats = quads.get(i);
            System.arraycopy(floats, 0, positions, i * 12, 12);
        }
        float[] texCoords = new float[12*texQuads.size()];
        for (int i = 0; i < texQuads.size(); i++) {
            float[] floats = texQuads.get(i);
            System.arraycopy(floats, 0, texCoords, i * 12, 12);
        }
        super(positions, texCoords);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof TextMesh)) return false;
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
