package tool.editor;

import engine.CoordinateMap;
import main.World;

import java.io.FileWriter;
import java.io.IOException;

public class JsonMaker {
    void main() throws IOException {
        String filename = IO.readln("Filename?");
        String id = IO.readln("ID?");
        World world = new World(CoordinateMap.newDefaultPackedMap(), filename, id);
        float brightness = Float.parseFloat(IO.readln("Brightness?"));
        world.setBrightness(brightness);
        JSONSaver.save(world, world.map).write(new FileWriter(world.address));
    }
}