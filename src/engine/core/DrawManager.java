package engine.core;

import engine.Maths;
import engine.core.graphics.TextMesh;
import engine.core.text.*;
import engine.core.shaders.Shader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL46.*;

public class DrawManager {
    protected final Screen screen;
    protected static int rectVAO;
    protected static int textVAO;
    protected static int textPositionsVBO;
    protected static int textTexCoordsVBO;
    protected static int rectPositionVBO;
    protected static int rectTexCoordsVBO;
    private final Vector3f CACHE_VEC1 = new Vector3f(), CACHE_VEC2 = new Vector3f();
    private final Matrix4f CACHE_MAT = new Matrix4f();
    protected static int lastBoundVAO;
    private static Texture lastBoundTexture;
    protected static Shader lastBoundShader;
    protected String colorVertexName = "src/engine/core/shaders/mainVertex.glsl";
    protected String texVertexName = "src/engine/core/shaders/mainVertex.glsl";
    protected String texFragName = "src/engine/core/shaders/mainFragment.glsl";
    protected String textVertexName = "src/engine/core/shaders/mainVertex.glsl";
    protected String textFragName = "src/engine/core/shaders/mainFragment.glsl";
    protected String colorFragName = "src/engine/core/shaders/untexturedFragment.glsl";
    protected Shader textured = new Shader(colorVertexName, texFragName);
    protected Shader text = new Shader(colorVertexName, textFragName);
    protected Shader plainColor = new Shader(texVertexName, colorFragName);
    private int drawCalls = 0;
    public DrawManager(Screen screen) {
        this.screen = screen;
    }
    public static void init() {
        float[] rectPositions = {
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,0
        };
        float[] rectTexCoords = {
                0,0,
                1,0,
                1,1,
                0,1,
                0,0,
                1,1
        };
        rectVAO = glGenVertexArrays();
        textVAO = glGenVertexArrays();
        glBindVertexArray(rectVAO);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        rectPositionVBO = glGenBuffers();
        rectTexCoordsVBO = glGenBuffers();
        storeDataInAttributeList(0, rectPositions, rectPositionVBO);
        storeDataInAttributeList(1, rectTexCoords, rectTexCoordsVBO);
        glBindVertexArray(textVAO);
        textPositionsVBO = glGenBuffers();
        textTexCoordsVBO = glGenBuffers();

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, textPositionsVBO);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, textTexCoordsVBO);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindVertexArray(0);
        lastBoundVAO = -1;
    }
    public static void swapTexture(Texture texture) {
        if (lastBoundTexture == texture)
            return;
        if (texture == null)
            glBindTexture(GL_TEXTURE_2D, 0);
        else if (!texture.bindBehaviourEquals(lastBoundTexture))
            texture.bind();
        lastBoundTexture = texture;
    }
    public void destroy() {
        plainColor.destroy();
        textured.destroy();
        text.destroy();
    }
    public static void destroyVAO() {
        glUseProgram(0);
        glBindVertexArray(0);
        glDeleteVertexArrays(rectVAO);
        glDeleteBuffers(rectPositionVBO);
        glDeleteBuffers(rectTexCoordsVBO);
        glDeleteVertexArrays(textVAO);
        glDeleteBuffers(textTexCoordsVBO);
        glDeleteBuffers(textPositionsVBO);
    }
    public static void storeDataInAttributeList(int attributeNumber, float[] data, int vboID) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    protected static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /// RENDERING

    public void rect(Color color, float x, float y, float width, float height) {
        CACHE_VEC1.set(x/screen.getAspectRatio(),y,0);
        CACHE_VEC2.set(width/screen.getAspectRatio(), height, 0);
        rect(color, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2));
    }
    public static void bindShader(final @NotNull Shader shader) {
        if (!shader.equals(lastBoundShader)) {
            shader.bind();
            lastBoundShader = shader;
        }
    }
    public void rect(final @NotNull Color color, final @NotNull Matrix4f transform) {
        bindShader(plainColor);
        plainColor.loadColor(plainColor.colorLocation, color);
        plainColor.loadMatrix(plainColor.transformLocation, transform);
        if (lastBoundVAO != rectVAO) {
            glBindVertexArray(rectVAO);
            lastBoundVAO = rectVAO;
        }
        swapTexture(null);
        plainColor.loadTexCoords(null, plainColor.texCoordsLocation);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        ++drawCalls;
    }
    public void image(final Texture tex, float x, float y, float width, float height) {
        tintedImage(tex, Color.white, x, y, width, height);
    }
    public void image(Texture tex, Matrix4f transform) {
        tintedImage(tex, Color.white, transform);
    }
    public void tintedImage(Texture tex, Color col, Matrix4f transform) {
        bindShader(textured);
        textured.loadColor(textured.colorLocation, col);
        textured.loadMatrix(textured.transformLocation, transform);
        if (lastBoundVAO != rectVAO) {
            glBindVertexArray(rectVAO);
            lastBoundVAO = rectVAO;
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        swapTexture(tex);
        textured.loadTexCoords(tex, textured.texCoordsLocation);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        ++drawCalls;
    }
    public void switchColorVertexShader(String currentAddress) {
        plainColor.destroy();
        colorVertexName = currentAddress;
        plainColor = new Shader(colorVertexName, colorFragName);
    }
    public void switchTextureVertexShader(String currentAddress) {
        textured.destroy();
        texVertexName = currentAddress;
        textured = new Shader(texVertexName, texFragName);
    }
    public void switchColorFragmentShader(String address) {
        plainColor.destroy();
        colorFragName = address;
        plainColor = new Shader(colorVertexName, colorFragName);
    }public void switchTextVertexShader(String currentAddress) {
        text.destroy();
        textVertexName = currentAddress;
        text = new Shader(textVertexName, textFragName);
    }
    public void switchTextFragmentShader(String address) {
        text.destroy();
        textFragName = address;
        text = new Shader(textVertexName, textFragName);
    }
    public void switchTextureFragmentShader(String address) {
        textured.destroy();
        texFragName = address;
        textured = new Shader(texVertexName, texFragName);
    }
    public void tintedImage(final Texture tex, final Color c, final float x, final float y, final float width, final float height) {
        bindShader(textured);
        CACHE_VEC1.set(x/screen.getAspectRatio(),y,0);
        CACHE_VEC2.set(width/screen.getAspectRatio(), height, 0);
        tintedImage(tex, c, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2));
    }
    public static final float SIZE_PER_FONT_SIZE = .01f;
    public static final float DEMO_WIDTH = 22;
    public static final float DEMO_HEIGHT = 54;
    public static final float DEMO_X_ADVANCE = 28;
    public static final float NEW_LINE = 70;

    public void string(String text, Color color, float x, float y, Font font, float size) {
        paragraph(text, color, x, y, font, size, 1000000);
    }

    /**
     * Renders a paragraph of text while considering line breaks and width limits.
     * This method calculates positions and texture coordinates for glyphs in the provided text
     * and displays them using specified font, size, and other attributes.
     * <br><br><b>
     * IMPORTANT: DO NOT USE THIS METHOD FOR STRINGS THAT LAST ANY LONGER THAN 0.5 SECONDS. THIS METHOD IS EXPENSIVE IN TERMS OF MEMORY USAGE.
     * <br>
     * Construct a {@link TextMesh} instead.</b>
     *
     * @param text The text content to be rendered. Can be null, which renders "null" instead.
     * @param color The color to be applied to the rendered text.
     * @param x The x-coordinate for positioning the paragraph.
     * @param y The y-coordinate for positioning the paragraph.
     * @param font The font to be used for rendering the text.
     * @param size The size of the font to be used.
     * @param paragraphWidthLimit The maximum width for a single line of the paragraph before wrapping.
     */
    public void paragraph(final @Nullable String text, final @NotNull Color color, final float x, final float y, final @NotNull Font font, final float size, final float paragraphWidthLimit) {
        paragraphCore(Objects.requireNonNullElse(text, "null"), color, x, y, font, size, paragraphWidthLimit);
    }
    public void paragraphCore(final @NotNull String text, final @NotNull Color color, final float x, final float y, final @NotNull Font font, final float size, final float paragraphWidthLimit) {
        bindShader(this.text);
        float xPosition = 0;
        float yPosition = 0;
        final Texture bitmap = font.getBitmap();
        final float[] positions = new float[12*text.length()];
        final float[] texCoords = new float[12*text.length()];
        Glyph glyph;
        for (int i = 0; i < text.length(); i++ ) {
            char c = text.charAt(i);
            if (c == '\n') {
                xPosition = 0;
                yPosition -= SIZE_PER_FONT_SIZE*size*NEW_LINE;
                continue;
            }
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
            float texHeight0 = glyphY/bitmap.getHeight();
            float texHeight1 = texHeight0+(absoluteHeight/bitmap.getHeight());
            float texWidth0 = glyphX/bitmap.getWidth();
            float texWidth1 = texWidth0+(absoluteWidth/bitmap.getWidth());
            positions[12*i] = xOffset + xPosition;
            positions[12*i+1] = demoHeight - yOffset + yPosition;
            positions[12*i+2] = width + xOffset + xPosition;
            positions[12*i+3] = demoHeight - yOffset + yPosition;
            positions[12*i+4] = width + xOffset + xPosition;
            positions[12*i+5] = demoHeight - yOffset - height + yPosition;
            positions[12*i+6] = xOffset + xPosition;
            positions[12*i+7] = demoHeight - yOffset - height + yPosition;
            positions[12*i+8] = xOffset + xPosition;
            positions[12*i+9] = demoHeight - yOffset + yPosition;
            positions[12*i+10] = width + xOffset + xPosition;
            positions[12*i+11] = demoHeight - yOffset - height + yPosition;

            texCoords[12*i] = texWidth0;
            texCoords[12*i+1] = texHeight0;
            texCoords[12*i+2] = texWidth1;
            texCoords[12*i+3] = texHeight0;
            texCoords[12*i+4] = texWidth1;
            texCoords[12*i+5] = texHeight1;
            texCoords[12*i+6] = texWidth0;
            texCoords[12*i+7] = texHeight1;
            texCoords[12*i+8] = texWidth0;
            texCoords[12*i+9] = texHeight0;
            texCoords[12*i+10] = texWidth1;
            texCoords[12*i+11] = texHeight1;
            xPosition += xAdvance;
            if (xPosition >= SIZE_PER_FONT_SIZE*paragraphWidthLimit) {
                xPosition = 0;
                yPosition -= SIZE_PER_FONT_SIZE*size*NEW_LINE;
            }
        }
        if (lastBoundVAO != textVAO) {
            glBindVertexArray(textVAO);
            lastBoundVAO = textVAO;
        }
        storeDataInAttributeList(0, positions, textPositionsVBO);
        storeDataInAttributeList(1, texCoords, textTexCoordsVBO);
        CACHE_VEC1.set(x/screen.getAspectRatio(),y,0);
        CACHE_VEC2.set(1/screen.getAspectRatio(), 1, 1);
        var mat = Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2);
        this.text.loadMatrix(this.text.transformLocation, mat);
        this.text.loadColor(this.text.colorLocation, color);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        swapTexture(bitmap);
        this.text.loadTexCoords(bitmap, this.text.texCoordsLocation);
        glDrawArrays(GL_TRIANGLES, 0, positions.length/2);
        ++drawCalls;
    }

    public Screen getScreen() {
        return screen;
    }

    public String getColorFragName() {
        return colorFragName;
    }

    public String getTextFragName() {
        return textFragName;
    }

    public String getTextVertexName() {
        return textVertexName;
    }

    public String getTexFragName() {
        return texFragName;
    }

    public String getTexVertexName() {
        return texVertexName;
    }

    public String getColorVertexName() {
        return colorVertexName;
    }

    public Shader getPlainColor() {
        return plainColor;
    }

    public Shader getText() {
        return text;
    }

    public Shader getTextured() {
        return textured;
    }

    public static int getRectVAO() {
        return rectVAO;
    }

    public static int getTextVAO() {
        return textVAO;
    }

    private static int getTextPositionsVBO() {
        return textPositionsVBO;
    }

    private static int getRectPositionVBO() {
        return rectPositionVBO;
    }

    private static int getTextTexCoordsVBO() {
        return textTexCoordsVBO;
    }

    private static int getRectTexCoordsVBO() {
        return rectTexCoordsVBO;
    }

    public static int getLastBoundVAO() {
        return lastBoundVAO;
    }

    public static Shader getLastBoundShader() {
        return lastBoundShader;
    }

    public static void setLastBoundShader(Shader s) {
        lastBoundShader = s;
    }

    public static void setLastBoundVAO(int vao) {
        lastBoundVAO = vao;
    }
    public void renderAsText(TextMesh mesh, Font font, Color color, float x, float y) {
        CACHE_VEC1.set(x/screen.getAspectRatio(),y,0);
        CACHE_VEC2.set(.01f/screen.getAspectRatio(), .01f, .01f);
        mesh.render(font.getBitmap(), text, color, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2));
        // no draw call tracking for meshes
    }
    public int resetDrawCalls() {
        int drawCalls = this.drawCalls;
        this.drawCalls = 0;
        return drawCalls;
    }
}
