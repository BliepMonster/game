package tiles;

import main.Camera;

import java.awt.*;

public class TileType {
    public final boolean standable;
    public final Color color;
    public TileType(boolean standable, Color color) {
        this.standable = standable;
        this.color = color;
    }
    public void render(Camera camera, int x, int y) {
        camera.rect(color, x, y, 1, 1);
    }
    public static final int TILETYPE_COUNT = 2;
    public static final int TILETYPE_GRASS = 0;
    public static final int TILETYPE_WALL = 1;
    public static final TileType[] types = new TileType[TILETYPE_COUNT];
    static {
        types[TILETYPE_GRASS] = new TileType(true, new Color(0, 200, 0));
        types[TILETYPE_WALL] = new TileType(false, Color.RED);
    }
}
