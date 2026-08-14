package saving;

import engine.CoordinateMap;
import engine.json.JsonObject;
import engine.saving.objects.SaveFile;
import engine.saving.TokenizedSaveFile;
import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedObject;
import main.GameContext;
import main.World;
import main.WorldRegistry;
import tool.editor.JSONLoader;
import tool.editor.JSONSaver;

import java.util.ArrayList;

public record SavedGame(SavedPlayer player, SavedHome home, SavedQuests quests, SavedAttributeData attributes) {
    public SaveFile save() {
        SaveFile file = new TokenizedSaveFile();
        file.put("player", player.save());
        file.put("world", JSONSaver.save(home.home(), home.home().map).toSaveFile());
        file.put("quests", quests.save());
        file.put("attributes", attributes.save());
        return file;
    }
    public SavedGame(SaveFile file, GameContext context) {
        World world = new World(CoordinateMap.newDefaultPackedMap(), "res/maps/house.json", "HOUSE");
        if (file.get("world") != null)
            JSONLoader.loadWorld((JsonObject) file.get("world").toJSON(), world.map, context);
        else world = WorldRegistry.get("HOUSE");
        this(
                new SavedPlayer(file.get("player")),
                new SavedHome(world),
                file.get("quests") == null ? SavedQuests.empty() : new SavedQuests(file.get("quests")),
                file.get("attributes") == null
                        ? new SavedAttributeData(new String[0])
                        : new SavedAttributeData(
                                ((ArrayList<SavedObject>) file.get("attributes").get())
                                        .stream().map(obj -> (String) obj.get()).toArray(String[]::new)
                )
        );
    }
}
