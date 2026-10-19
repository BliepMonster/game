package saving;

import engine.CoordinateMap;
import engine.json.JsonObject;
import engine.saving.objects.SaveFile;
import engine.saving.TokenizedSaveFile;
import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedDictionary;
import engine.saving.objects.SavedDouble;
import engine.saving.objects.SavedObject;
import main.GameContext;
import main.World;
import main.WorldRegistry;
import tool.editor.JSONLoader;
import tool.editor.JSONSaver;

import java.util.ArrayList;
import java.util.HashMap;

public record SavedGame(SavedPlayer player, SavedHome home, SavedQuests quests, SavedAttributeData attributes, SavedObject data, int ticks) {
    public SaveFile save() {
        SaveFile file = new TokenizedSaveFile();
        file.put("player", player.save());
        file.put("world", JSONSaver.save(home.home(), home.home().map).toSaveFile());
        file.put("quests", quests.save());
        file.put("attributes", attributes.save());
        file.put("data", data);
        file.put("ticks", new SavedDouble(ticks));
        return file;
    }
    public static int readTicks(SaveFile file) {
        var ticks = file.get("ticks");
        return ticks != null ? (int) (double) ticks.get() : 0;
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
                ), file.map.containsKey("data") ? file.get("data") : new SavedDictionary(new HashMap<>()),
                readTicks(file)
        );
    }
}
