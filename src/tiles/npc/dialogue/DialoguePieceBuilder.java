package tiles.npc.dialogue;

import crafting.FullItemStorage;
import engine.core.text.Font;
import main.GameContext;

public interface DialoguePieceBuilder {
    DialoguePiece build(Font font, DialogueController controller, GameContext context, FullItemStorage storage);
}
