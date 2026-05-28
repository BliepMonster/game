package main;

import engine.core.DrawManager;
import engine.core.Texture;
import entities.Player;

import java.awt.*;
import java.util.ArrayList;

public class Camera {
    record RenderJob(Runnable function, float z) {}
    private final Player player;
    private float tileSize;
    private final DrawManager draw;
    private ArrayList<RenderJob> renderJobs = new ArrayList<>();
    public Camera(Player player, float tileSize, DrawManager draw) {
        this.player = player;
        this.tileSize = tileSize;
        this.draw = draw;
    }
    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }
    public void rect(Color color, float x, float y, float w, float h) {
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
        draw.image(texture, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize);
    }
    public void tintedImage(Texture texture, Color color, float x, float y, float w, float h) {
        draw.tintedImage(texture, color, (x-player.getX())*tileSize, (y-player.getY())*tileSize, w*tileSize, h*tileSize);
    }
}
