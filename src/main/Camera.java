package main;

import engine.core.DrawManager;
import engine.core.Texture;
import entities.Player;
import lighting.LightSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Camera {
    record RenderJob(Runnable function, float z) {}
    private final Player player;
    private float tileSize;
    private final DrawManager draw;
    private final ArrayList<RenderJob> renderJobs = new ArrayList<>();
    private final HashSet<LightSource> light = new HashSet<>();
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
    private float computeBrightness(float x, float y) {
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

    public void rect(Color color, float x, float y, float w, float h) {
        float bx = x + w/2, by = y + h/2;
        rect(color, x, y, w, h, computeBrightness(bx, by));
    }
    public void rect(Color color, float x, float y, float w, float h, float brightness) {
        draw.setLightLevel(brightness);
        draw.rect(color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize);
    }
    public void addRenderJob(Runnable function, float z) {
        renderJobs.add(new RenderJob(function, z));
    }
    public void renderAllJobs() {
        renderJobs.sort((a, b) -> Float.compare(a.z, b.z));
        for (RenderJob job : renderJobs) {
            job.function.run();
        }
        renderJobs.clear();
    }
    public void image(Texture texture, float x, float y, float w, float h) {
        float bx = x + w/2, by = y + h/2;
        image(texture, x, y, w, h, computeBrightness(bx, by));
    }
    public void image(Texture texture, float x, float y, float w, float h, float brightness) {
        draw.setLightLevel(brightness);
        draw.image(texture, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize);
    }
    public void tintedImage(Texture texture, Color color, float x, float y, float w, float h) {
        float bx = x + w/2, by = y + h/2;
        tintedImage(texture, color, x, y, w, h, computeBrightness(bx, by));
    }
    public void tintedImage(Texture texture, Color color, float x, float y, float w, float h, float brightness) {
        draw.setLightLevel(brightness);
        draw.tintedImage(texture, color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize);
    }
}
