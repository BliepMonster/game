package tiles.npc;

import engine.core.Texture;

public enum Currency {
    COINS,
    ;
    public static final Texture COIN_TEXTURE = new Texture("res/bred.png");
    public Texture getTexture() {
        return switch (this) {
            case COINS -> COIN_TEXTURE;
        };
    }
}
