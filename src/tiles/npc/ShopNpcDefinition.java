package tiles.npc;

import engine.core.Texture;

public record ShopNpcDefinition(String name, String id, Texture texture, float height, ShopEntry... entries) {
}
