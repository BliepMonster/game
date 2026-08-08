package saving;

import engine.CoordinateMap;
import engine.json.JsonObject;
import engine.saving.objects.SaveFile;
import engine.saving.TokenizedSaveFile;
import main.GameContext;
import main.World;
import main.WorldRegistry;
import tool.editor.JSONLoader;
import tool.editor.JSONSaver;

public record SavedGame(SavedPlayer player, SavedHome home) {
    public SaveFile save() {
        SaveFile file = new TokenizedSaveFile();
        file.put("player", player.save());
        file.put("world", JSONSaver.save(home.home(), home.home().map).toSaveFile());
        return file;
    }
    public SavedGame(SaveFile file, GameContext context) {
        World world = new World(CoordinateMap.newDefaultPackedMap(), "res/maps/house.json", "HOUSE");
        if (file.get("world") != null)
            JSONLoader.loadWorld((JsonObject) file.get("world").toJSON(), world.map, context);
        else world = WorldRegistry.get("HOUSE");
        this(
                new SavedPlayer(file.get("player")),
                new SavedHome(world)
        );
    }
}
