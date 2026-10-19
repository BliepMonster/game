package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

public class ConditionalDialoguePiece extends DialoguePiece {
    DialoguePiece yes, no;
    String attrib;
    GameContext gameContext;

    public ConditionalDialoguePiece(DialoguePiece yes, DialoguePiece no, String attrib, DialogueController controller) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(0), new RelativeSizeConstraint(0), controller);
        this.yes = yes;
        this.no = no;
        this.attrib = attrib;
    }
    public void bindContext(GameContext context) {
        this.gameContext = context;
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        if (gameContext.attributes().contains(attrib)) {
            yes.manage(draw, pressed);
        } else no.manage(draw, pressed);
    }

    @Override
    public void display(DrawManager draw, float x, float y, float width, float height) {

    }

    @Override
    public void detectClick(Screen screen, float x, float y) {

    }

    @Override
    public void detectContinuousPress(Screen screen, float x, float y) {

    }
}
