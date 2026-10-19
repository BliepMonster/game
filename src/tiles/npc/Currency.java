package tiles.npc;

import engine.core.textures.Texture;

public enum Currency {
    COINS,
    ;
    public static final Texture COIN_TEXTURE = new Texture("res/textures/icons/coin.png");
    public Texture getTexture() {
        return switch (this) {
            case COINS -> COIN_TEXTURE;
        };
    }
}
