package tiles;

import engine.CoordinateMap;

import java.util.Optional;

public class Region {
    private final CoordinateMap<Tile> map;
    public final int xOffset, yOffset;
    public Region(CoordinateMap<Tile> map, int xOffset, int yOffset) {
        this.map = map;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    public Optional<Tile> get(int x, int y) {
        return map.get(x+xOffset, y+yOffset);
    }
    public void put(int x, int y, Tile tile) {
        map.put(x+xOffset, y+yOffset, tile);
    }
}
