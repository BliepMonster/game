package tiles.npc;

import engine.core.Texture;
import tiles.npc.dialogue.DialoguePieceBuilder;

public record NpcDefinition(String name, String id, Texture texture, float height, DialoguePieceBuilder[] pieces) {
}
