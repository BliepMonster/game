package saving;

import engine.saving.SaveFile;
import engine.saving.TokenizedSaveFile;

public record SavedGame(SavedPlayer player) {
    public SaveFile save() {
        SaveFile file = new TokenizedSaveFile();
        file.put("player", player.save());
        return file;
    }
    public SavedGame(SaveFile file) {
        this(
                new SavedPlayer(file.get("player"))
        );
    }
}
