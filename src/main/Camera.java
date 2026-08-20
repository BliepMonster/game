package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.Texture;
import entities.Player;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Objects;

public class Camera {
    record RenderJob(Runnable function, float z) {}
    private final Player player;
    private float tileSize;
    private final DrawManager draw;
    private final List<CameraEffect> effects = new ArrayList<>();
    private final PriorityQueue<RenderJob> renderJobs = new PriorityQueue<>((a, b) -> Float.compare(a.z, b.z));
    private final HashSet<LightSource> light = new HashSet<>();
    private float[][] lightmap;
    private int lightmapW, lightmapH;
    private float lightmapOriginX, lightmapOriginY;
    public float defaultBrightness = 1f;
    public Camera(Player player, float tileSize, DrawManager draw) {
        this.player = player;
        this.tileSize = tileSize;
        this.draw = draw;
    }

    public float getTileSize() {
        return tileSize;
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
        float px = player.getX();
        float py = player.getY();
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
        draw.litRect(color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void circle(Color color, float x, float y, float r) {
        draw.circle(color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, r*tileSize);
    }
    public void addRenderJob(Runnable function, float z) {
        renderJobs.add(new RenderJob(function, z));
    }
    public void renderAllJobs() {
        while (!renderJobs.isEmpty()) {
            renderJobs.poll().function.run();
        }
    }
    public void image(Texture texture, float x, float y, float w, float h) {
        draw.litImage(texture, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void tintedImage(Texture texture, Color color, float x, float y, float w, float h) {
        draw.litTintedImage(texture, color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void batchedImage(Texture texture, float x, float y, float w, float h) {
        draw.batchLitImage(texture, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize,
                computeBrightness(x, y+h), computeBrightness(x+w, y+h), computeBrightness(x+w, y), computeBrightness(x, y));
    }
    public void flushBatch() {
        draw.flushLitBatch();
    }
    public void renderEffects(GameContext context) {
        effects.replaceAll(effect -> effect.getTimeLeft() < 0 ? effect.getSuccessor(context).orElse(null) : effect);
        effects.removeIf(Objects::isNull);
        effects.forEach(effect ->effect.draw(draw));
    }
    public void cone(Color color, float cx, float cy, float size, float aim, float sweep, int segments) {
        var verts = new float[(segments+1)*6];
        float invAspect = 1f/draw.getScreen().getAspectRatio();
        float sx = (cx-player.getX())*tileSize*invAspect;
        float sy = (cy-player.getY())*tileSize;
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
        draw.polygon(color, verts);
    }
    public void addEffect(CameraEffect effect) {
        effects.add(effect);
    }
}
