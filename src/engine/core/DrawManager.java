package engine.core;

import engine.Maths;
import engine.core.graphics.TextMesh;
import engine.core.text.*;
import engine.core.shaders.Shader;
import engine.core.textures.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.awt.Color;
import java.lang.Math;
import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL46.*;

public class DrawManager {
    protected final Screen screen;
    protected static int rectVAO;
    protected static int textVAO;
    protected static int batchVAO;
    protected static int batchVBO;
    private static int batchVBOCapacity;
    protected static int polygonVAO;
    protected static int polygonVBO;
    private static int polygonVBOCapacity;
    private static FloatBuffer polygonBuffer = BufferUtils.createFloatBuffer(512);
    private static float[] circleScratch = new float[32 * 6];
    protected static int textPositionsVBO;
    protected static int textTexCoordsVBO;
    protected static int rectPositionVBO;
    protected static int rectTexCoordsVBO;
    private static final Vector3f CACHE_VEC1 = new Vector3f();
    private static final Vector3f CACHE_VEC2 = new Vector3f();
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
    protected String litVertexName = "res/shaders/lighting_vertex.glsl";
    protected String litFragName = "res/shaders/lighting_fragment.glsl";
    protected String litColorFragName = "res/shaders/lighting_rfragment.glsl";
    protected String batchVertexName = "res/shaders/batch_vertex.glsl";
    protected String batchFragName = "res/shaders/batch_fragment.glsl";
    protected Shader textured = new Shader(colorVertexName, texFragName);
    protected Shader text = new Shader(colorVertexName, textFragName);
    protected Shader plainColor = new Shader(texVertexName, colorFragName);
    protected Shader litTextured = new Shader(litVertexName, litFragName);
    protected Shader litColor = new Shader(litVertexName, litColorFragName);
    protected Shader batchTextured = new Shader(batchVertexName, batchFragName);
    private static class BatchBuffer {
        private static final int INITIAL_FLOATS = 4096;
        private static final int FLOATS_PER_QUAD = 30;
        private FloatBuffer data = BufferUtils.createFloatBuffer(INITIAL_FLOATS);
        void beginQuad() {
            if (data.remaining() < FLOATS_PER_QUAD) {
                FloatBuffer grown = BufferUtils.createFloatBuffer(
                        java.lang.Math.max(data.capacity() * 2, data.capacity() + FLOATS_PER_QUAD));
                data.flip();
                grown.put(data);
                data = grown;
            }
        }
        void vertex(float x, float y, float u, float v, float light) {
            data.put(x).put(y).put(u).put(v).put(light);
        }
        boolean isEmpty() {
            return data.position() == 0;
        }
        FloatBuffer flipForUpload() {
            return data.flip();
        }
        void clear() {
            data.clear();
        }
    }
    private final java.util.HashMap<Texture, BatchBuffer> batches = new java.util.HashMap<>();
    private int drawCalls = 0;
    private float nextLightLevel = -1;

    public void setLightLevel(float level) {
        nextLightLevel = level;
    }
    private void applyLightLevel() {
        if (nextLightLevel < 0) return;
        plainColor.loadFloat(plainColor.lightLevelLocation, nextLightLevel);
        textured.loadFloat(textured.lightLevelLocation, nextLightLevel);
        text.loadFloat(text.lightLevelLocation, nextLightLevel);
        nextLightLevel = -1;
    }
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

        batchVAO = glGenVertexArrays();
        batchVBO = glGenBuffers();
        glBindVertexArray(batchVAO);
        glBindBuffer(GL_ARRAY_BUFFER, batchVBO);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 1, GL_FLOAT, false, 5 * Float.BYTES, 4 * Float.BYTES);
        glBindVertexArray(0);

        polygonVAO = glGenVertexArrays();
        polygonVBO = glGenBuffers();
        glBindVertexArray(polygonVAO);
        glBindBuffer(GL_ARRAY_BUFFER, polygonVBO);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);

        lastBoundVAO = -1;
        batchVBOCapacity = 0;
        polygonVBOCapacity = 0;
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
        litTextured.destroy();
        litColor.destroy();
        batchTextured.destroy();
    }
    public static void destroyVAO() {
        glUseProgram(0);
        glBindVertexArray(0);
        glDeleteVertexArrays(rectVAO);
        glDeleteBuffers(rectPositionVBO);
        glDeleteBuffers(rectTexCoordsVBO);
        glDeleteVertexArrays(textVAO);
        glDeleteBuffers(textTexCoordsVBO);
        glDeleteVertexArrays(batchVAO);
        glDeleteBuffers(batchVBO);
        glDeleteVertexArrays(polygonVAO);
        glDeleteBuffers(polygonVBO);
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
        applyLightLevel();
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
    public void circle(final @NotNull Color color, final float cx, final float cy, final float r, final int segments) {
        float invAspect = 1f / screen.getAspectRatio();
        float sx = cx * invAspect;
        float sy = cy;
        float sr = r * invAspect;
        if (circleScratch.length < segments * 6) circleScratch = new float[segments * 6];
        float[] verts = circleScratch;
        int idx = 0;
        for (int i = 0; i < segments; i++) {
            float a0 = (float) (2.0 * Math.PI * i / segments);
            float a1 = (float) (2.0 * Math.PI * (i + 1) / segments);
            verts[idx++] = sx;
            verts[idx++] = sy;
            verts[idx++] = sx + (float) Math.cos(a0) * sr;
            verts[idx++] = sy + (float) Math.sin(a0) * r;
            verts[idx++] = sx + (float) Math.cos(a1) * sr;
            verts[idx++] = sy + (float) Math.sin(a1) * r;
        }
        polygon(color, verts, segments * 6);
    }
    public void circle(final @NotNull Color color, final float cx, final float cy, final float r) {
        circle(color, cx, cy, r, 32);
    }
    public void polygon(final @NotNull Color color, final float[] verts) {
        polygon(color, verts, verts.length);
    }
    public void polygon(final @NotNull Color color, final float[] verts, final int floatCount) {
        bindShader(plainColor);
        applyLightLevel();
        plainColor.loadColor(plainColor.colorLocation, color);
        CACHE_MAT.identity();
        plainColor.loadMatrix(plainColor.transformLocation, CACHE_MAT);
        if (lastBoundVAO != polygonVAO) {
            glBindVertexArray(polygonVAO);
            lastBoundVAO = polygonVAO;
        }
        swapTexture(null);
        plainColor.loadTexCoords(null, plainColor.texCoordsLocation);
        glBindBuffer(GL_ARRAY_BUFFER, polygonVBO);
        if (polygonBuffer.capacity() < floatCount) {
            polygonBuffer = BufferUtils.createFloatBuffer(floatCount);
            polygonVBOCapacity = 0;
        }
        polygonBuffer.clear();
        polygonBuffer.put(verts, 0, floatCount);
        polygonBuffer.flip();
        if (floatCount > polygonVBOCapacity) {
            glBufferData(GL_ARRAY_BUFFER, polygonBuffer, GL_DYNAMIC_DRAW);
            polygonVBOCapacity = floatCount;
        } else {
            glBufferData(GL_ARRAY_BUFFER, (long) floatCount * Float.BYTES, GL_STREAM_DRAW);
            glBufferSubData(GL_ARRAY_BUFFER, 0, polygonBuffer);
        }
        glDrawArrays(GL_TRIANGLES, 0, floatCount/2);
        ++drawCalls;
    }
    public void image(final Texture tex, float x, float y, float width, float height) {
        tintedImage(tex, Color.white, x, y, width, height);
    }
    public void image(Texture tex, Matrix4f transform) {
        tintedImage(tex, Color.white, transform);
    }
    public void tintedImage(Texture tex, Color col, Matrix4f transform) {
        if (tex == null) return;
        bindShader(textured);
        applyLightLevel();
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
    public void litRect(Color color, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        CACHE_VEC1.set(x/screen.getAspectRatio(), y, 0);
        CACHE_VEC2.set(width/screen.getAspectRatio(), height, 0);
        litRect(color, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2), topLeft, topRight, bottomRight, bottomLeft);
    }
    public void litRect(final @NotNull Color color, final @NotNull Matrix4f transform, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        bindShader(litColor);
        litColor.loadColor(litColor.colorLocation, color);
        litColor.loadMatrix(litColor.transformLocation, transform);
        if (lastBoundVAO != rectVAO) {
            glBindVertexArray(rectVAO);
            lastBoundVAO = rectVAO;
        }
        swapTexture(null);
        litColor.loadTexCoords(null, litColor.texCoordsLocation);
        litColor.loadLightLevels(topLeft, topRight, bottomRight, bottomLeft);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        ++drawCalls;
    }
    public void litImage(Texture tex, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        litTintedImage(tex, Color.white, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft);
    }
    public void litTintedImage(Texture tex, Color col, float x, float y, float width, float height, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        CACHE_VEC1.set(x/screen.getAspectRatio(), y, 0);
        CACHE_VEC2.set(width/screen.getAspectRatio(), height, 0);
        litTintedImage(tex, col, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2), topLeft, topRight, bottomRight, bottomLeft);
    }
    public void litTintedImage(Texture tex, Color col, Matrix4f transform, float topLeft, float topRight, float bottomRight, float bottomLeft) {
        bindShader(litTextured);
        litTextured.loadColor(litTextured.colorLocation, col);
        litTextured.loadMatrix(litTextured.transformLocation, transform);
        if (lastBoundVAO != rectVAO) {
            glBindVertexArray(rectVAO);
            lastBoundVAO = rectVAO;
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        swapTexture(tex);
        litTextured.loadTexCoords(tex, litTextured.texCoordsLocation);
        litTextured.loadLightLevels(topLeft, topRight, bottomRight, bottomLeft);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        ++drawCalls;
    }
    public void batchLitImage(Texture tex, float x, float y, float width, float height,
                              float topLeft, float topRight, float bottomRight, float bottomLeft) {
        float ax = x/screen.getAspectRatio();
        float aw = width/screen.getAspectRatio();
        var buffer = batches.computeIfAbsent(tex, _ -> new BatchBuffer());
        buffer.beginQuad();
        float x1 = ax + aw;
        float y1 = y + height;
        buffer.vertex(ax,  y1, 0, 0, topLeft);
        buffer.vertex(x1,  y1, 1, 0, topRight);
        buffer.vertex(x1,  y,  1, 1, bottomRight);
        buffer.vertex(ax,  y,  0, 1, bottomLeft);
        buffer.vertex(ax,  y1, 0, 0, topLeft);
        buffer.vertex(x1,  y,  1, 1, bottomRight);
    }
    public void flushLitBatch() {
        for (var entry : batches.entrySet()) {
            var buffer = entry.getValue();
            if (buffer.isEmpty()) continue;
            drawBatched(entry.getKey(), buffer);
            buffer.clear();
        }
    }
    private void drawBatched(Texture tex, BatchBuffer batch) {
        bindShader(batchTextured);
        CACHE_MAT.identity();
        batchTextured.loadColor(batchTextured.colorLocation, Color.white);
        batchTextured.loadMatrix(batchTextured.transformLocation, CACHE_MAT);
        if (lastBoundVAO != batchVAO) {
            glBindVertexArray(batchVAO);
            lastBoundVAO = batchVAO;
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        swapTexture(tex);
        batchTextured.loadTexCoords(tex, batchTextured.texCoordsLocation);
        FloatBuffer data = batch.flipForUpload();
        int floats = data.remaining();
        glBindBuffer(GL_ARRAY_BUFFER, batchVBO);
        if (floats > batchVBOCapacity) {
            glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
            batchVBOCapacity = floats;
        } else {
            glBufferData(GL_ARRAY_BUFFER, (long) floats * Float.BYTES, GL_STREAM_DRAW);
            glBufferSubData(GL_ARRAY_BUFFER, 0, data);
        }
        glDrawArrays(GL_TRIANGLES, 0, floats/5);
        ++drawCalls;
    }
    public void switchBatchVertexShader(String currentAddress) {
        batchTextured.destroy();
        batchVertexName = currentAddress;
        batchTextured = new Shader(batchVertexName, batchFragName);
    }
    public void switchBatchFragmentShader(String address) {
        batchTextured.destroy();
        batchFragName = address;
        batchTextured = new Shader(batchVertexName, batchFragName);
    }
    public void switchLitVertexShader(String currentAddress) {
        litTextured.destroy();
        litColor.destroy();
        litVertexName = currentAddress;
        litTextured = new Shader(litVertexName, litFragName);
        litColor = new Shader(litVertexName, litColorFragName);
    }
    public void switchLitFragmentShader(String address) {
        litTextured.destroy();
        litFragName = address;
        litTextured = new Shader(litVertexName, litFragName);
    }
    public void switchLitColorFragmentShader(String address) {
        litColor.destroy();
        litColorFragName = address;
        litColor = new Shader(litVertexName, litColorFragName);
    }
    public static final float SIZE_PER_FONT_SIZE = .01f;

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
        applyLightLevel();
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
                yPosition -= SIZE_PER_FONT_SIZE*size*font.getLineHeight();
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
            float demoHeight = font.getBase()*SIZE_PER_FONT_SIZE*size;
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
                width = font.getAverageWidth()*SIZE_PER_FONT_SIZE*size;
                height = demoHeight;
                xAdvance = font.getAverageXAdvance()*SIZE_PER_FONT_SIZE*size;
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
            if (xPosition >= paragraphWidthLimit) {
                xPosition = 0;
                yPosition -= SIZE_PER_FONT_SIZE*size*font.getLineHeight();
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

    public Shader getLitTextured() {
        return litTextured;
    }

    public Shader getLitColor() {
        return litColor;
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
        CACHE_VEC2.set(1f/screen.getAspectRatio(), 1, 1);
        mesh.render(font.getBitmap(), text, color, Maths.loadTransform(CACHE_MAT, CACHE_VEC1, CACHE_VEC2));
        // no draw call tracking for meshes
    }
    public int resetDrawCalls() {
        int drawCalls = this.drawCalls;
        this.drawCalls = 0;
        return drawCalls;
    }
}
