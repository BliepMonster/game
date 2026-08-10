package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.Texture;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.*;

public class TextDialogue extends DialoguePiece {
    private final Texture texture;
    private final String name, message;
    private long milliStarted;
    private final engine.core.text.Font font;

    public TextDialogue(DialogueController controller, Texture texture, String name, String message, engine.core.text.Font font) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(.8f), new RelativeSizeConstraint(.6f), controller);
        this.texture = texture;
        this.name = name;
        this.message = message;
        this.font = font;
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {
        draw.rect(Color.white, x, y, width, height);
        draw.rect(Color.gray, x+width/2, y, width/2, height);
        draw.rect(Color.black, x, y, width/2, height/3);
        int n = (int)(System.currentTimeMillis()-milliStarted)/50;
        String s = message.substring(0, Math.min(n, message.length()));
        float size = .1f;
        draw.paragraph(s, Color.red, x+width/2, y+height-font.getHeight(size), font, size, .4f);
        draw.string(name, Color.red, x, y, font, size);
        draw.image(texture, x, y+.2f, .4f, .4f);
    }

    @Override
    public void start() {
        milliStarted = System.currentTimeMillis();
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (System.currentTimeMillis()-milliStarted > 500)
            advance();
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
