package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.textures.Texture;
import engine.core.graphics.TextMesh;
import entities.Player;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Objects;

public class Camera {
    private static final class RenderJob {
        Runnable function;
        float z;
    }
    private static final Comparator<RenderJob> JOB_ORDER = (a, b) -> Float.compare(a.z, b.z);
    private final Player player;
    private float tileSize;
    private float interpolationAlpha = 0f;
    private final DrawManager draw;
    private final List<CameraEffect> effects = new ArrayList<>();
    private final ArrayList<RenderJob> renderJobs = new ArrayList<>();
    private final ArrayList<RenderJob> jobPool = new ArrayList<>();
    private final HashSet<LightSource> light = new HashSet<>();
    private float[][] lightmap;
    private int lightmapW, lightmapH;
    private float lightmapOriginX, lightmapOriginY;
    public float defaultBrightness = 1f;
    private float shakeIntensity;
    private float shakeX, shakeY;
    private float prevShakeX, prevShakeY;
    private static final float SHAKE_DECAY = .7f;
    private static final float MAX_SHAKE = .75f;
    public Camera(Player player, float tileSize, DrawManager draw) {
        this.player = player;
        this.tileSize = tileSize;
        this.draw = draw;
    }

    public float getTileSize() {
        return tileSize;
    }

    public void setInterpolationAlpha(float interpolationAlpha) {
        this.interpolationAlpha = interpolationAlpha;
    }

    public float getInterpolationAlpha() {
        return interpolationAlpha;
    }

    private float getRenderPlayerX() {
        return player.getRenderX() + getShakeOffsetX();
    }

    private float getRenderPlayerY() {
        return player.getRenderY() + getShakeOffsetY();
    }

    public void addShake(float magnitude) {
        shakeIntensity = Math.min(shakeIntensity + magnitude, MAX_SHAKE);
    }

    public void tick() {
        prevShakeX = shakeX;
        prevShakeY = shakeY;
        shakeIntensity *= SHAKE_DECAY;
        if (shakeIntensity < .001f) {
            shakeIntensity = 0;
            shakeX = 0;
            shakeY = 0;
            prevShakeX = 0;
            prevShakeY = 0;
            return;
        }
        double angle = Math.random() * Math.PI * 2;
        shakeX = (float) Math.cos(angle) * shakeIntensity;
        shakeY = (float) Math.sin(angle) * shakeIntensity;
    }

    private float getShakeOffsetX() {
        return prevShakeX + (shakeX - prevShakeX) * interpolationAlpha;
    }

    private float getShakeOffsetY() {
        return prevShakeY + (shakeY - prevShakeY) * interpolationAlpha;
    }

    public DrawManager getDraw() {
        return draw;
    }

    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }

    public void clearLights() {
        light.clear();
    }
    public void addLight(LightSource ls) {
        light.add(ls);
    }
    public void rebuildLightmap(Screen screen) {
        float halfW = screen.getAspectRatio() / tileSize;
        float halfH = 1f / tileSize;
        float px = getRenderPlayerX();
        float py = getRenderPlayerY();
        float minX = px - halfW - 2;
        float minY = py - halfH - 2;
        float maxX = px + halfW + 2;
        float maxY = py + halfH + 2;
        int w = (int) Math.ceil(maxX - minX) + 1;
        int h = (int) Math.ceil(maxY - minY) + 1;
        lightmapW = w;
        lightmapH = h;
        lightmapOriginX = minX;
        lightmapOriginY = minY;
        if (lightmap == null || lightmap.length < h || lightmap[0].length < w) {
            lightmap = new float[h][w];
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float wx = minX + x;
                float wy = minY + y;
                float b = defaultBrightness;
                for (LightSource ls : light) {
                    float dx = wx - ls.getX();
                    float dy = wy - ls.getY();
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    float contribution = ls.getLightLevel() / (1f + dist);
                    if (contribution > b) b = contribution;
                }
                lightmap[y][x] = b;
            }
        }
    }
    private float computeBrightness(float x, float y) {
        float fx = x - lightmapOriginX;
        float fy = y - lightmapOriginY;
        int ix = (int) Math.floor(fx);
        int iy = (int) Math.floor(fy);
        if (ix < 0 || iy < 0 || ix >= lightmapW - 1 || iy >= lightmapH - 1) {
            float b = defaultBrightness;
            for (LightSource ls : light) {
                float dx = x - ls.getX();
                float dy = y - ls.getY();
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                float contribution = ls.getLightLevel() / (1f + dist);
                if (contribution > b) b = contribution;
            }
            return b;
        }
        float fracX = fx - ix;
        float fracY = fy - iy;
        float b00 = lightmap[iy][ix];
        float b10 = lightmap[iy][ix + 1];
        float b01 = lightmap[iy + 1][ix];
        float b11 = lightmap[iy + 1][ix + 1];
        return b00 * (1 - fracX) * (1 - fracY)
             + b10 * fracX * (1 - fracY)
             + b01 * (1 - fracX) * fracY
             + b11 * fracX * fracY;
    }

    public void rect(Color color, float x, float y, float w, float h) {
        draw.litRect(color, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void circle(Color color, float x, float y, float r) {
        draw.circle(color, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize, r*tileSize);
    }
    public void addRenderJob(Runnable function, float z) {
        RenderJob job = jobPool.isEmpty() ? new RenderJob() : jobPool.remove(jobPool.size() - 1);
        job.function = function;
        job.z = z;
        renderJobs.add(job);
    }
    public void renderAllJobs() {
        if (renderJobs.isEmpty()) return;
        renderJobs.sort(JOB_ORDER);
        for (int i = 0; i < renderJobs.size(); ++i) {
            RenderJob job = renderJobs.get(i);
            job.function.run();
            job.function = null;
        }
        jobPool.addAll(renderJobs);
        renderJobs.clear();
    }
    public void image(Texture texture, float x, float y, float w, float h) {
        draw.litImage(texture, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void tintedImage(Texture texture, Color color, float x, float y, float w, float h) {
        draw.litTintedImage(texture, color, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void batchedImage(Texture texture, float x, float y, float w, float h) {
        draw.batchLitImage(texture, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void renderAsText(TextMesh mesh, Color color, float x, float y) {
        draw.renderAsText(mesh, Main.font, color, (x-getRenderPlayerX())*tileSize, (y-getRenderPlayerY())*tileSize);
    }
    public void flushBatch() {
        draw.flushLitBatch();
    }
    public void renderEffects(GameContext context) {
        effects.replaceAll(effect -> effect.getTimeLeft() < 0 ? effect.getSuccessor(context).orElse(null) : effect);
        effects.removeIf(Objects::isNull);
        effects.forEach(effect ->effect.draw(draw));
    }
    private float[] coneScratch = new float[20 * 6];
    public void cone(Color color, float cx, float cy, float size, float aim, float sweep, int segments) {
        if (coneScratch.length < segments * 6) coneScratch = new float[segments * 6];
        float[] verts = coneScratch;
        float invAspect = 1f/draw.getScreen().getAspectRatio();
        float sx = (cx-getRenderPlayerX())*tileSize*invAspect;
        float sy = (cy-getRenderPlayerY())*tileSize;
        float ss = size*tileSize;
        float start = aim - sweep/2;
        int idx = 0;
        for (int k = 0; k < segments; ++k) {
            float a0 = start + (sweep/segments)*k;
            float a1 = start + (sweep/segments)*(k+1);
            verts[idx++] = sx;
            verts[idx++] = sy;
            verts[idx++] = sx + (float)Math.cos(a0)*ss*invAspect;
            verts[idx++] = sy + (float)Math.sin(a0)*ss;
            verts[idx++] = sx + (float)Math.cos(a1)*ss*invAspect;
            verts[idx++] = sy + (float)Math.sin(a1)*ss;
        }
        draw.polygon(color, verts, segments * 6);
    }
    public void addEffect(CameraEffect effect) {
        effects.add(effect);
    }
}
