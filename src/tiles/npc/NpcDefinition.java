package tiles.npc;

import engine.core.Texture;
import tiles.npc.dialogue.DialoguePieceBuilder;

public record NpcDefinition(String name, String id, Texture texture, float height, float zOffset, boolean standable, DialoguePieceBuilder[] pieces) {
}
