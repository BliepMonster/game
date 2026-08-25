package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.core.Texture;
import engine.core.graphics.TextMesh;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

import java.awt.*;

public class TextDialogue extends DialoguePiece {
    private static final float SIZE = .1f;
    private final Texture texture;
    private final String name, message;
    private long milliStarted;
    private final engine.core.text.Font font;
    private TextMesh messageMesh, nameMesh;
    private int shownChars = -1;

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
        int shown = Math.min(n, message.length());
        if (shown != shownChars) {
            if (messageMesh != null) messageMesh.close();
            messageMesh = new TextMesh(message.substring(0, shown), font, SIZE, .4f/100f);
            messageMesh.create();
            shownChars = shown;
        }
        if (nameMesh == null) {
            nameMesh = new TextMesh(name, font, SIZE, 10000f);
            nameMesh.create();
        }
        draw.renderAsText(messageMesh, font, Color.red, x+width/2, y+height-font.getHeight(SIZE));
        draw.renderAsText(nameMesh, font, Color.red, x, y);
        draw.image(texture, x, y+.2f, .4f, .4f);
    }

    @Override
    public void start() {
        milliStarted = System.currentTimeMillis();
        shownChars = -1;
    }

    @Override
    public void detectClick(Screen screen, float x, float y) {
        if (System.currentTimeMillis()-milliStarted > 500)
            advance();
    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }

    @Override
    public void free() {
        if (messageMesh != null) {
            messageMesh.close();
            messageMesh = null;
            shownChars = -1;
        }
        if (nameMesh != null) {
            nameMesh.close();
            nameMesh = null;
        }
    }
}
