package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

public class DialogueController extends Component {
    protected int position;
    protected DialoguePiece[] pieces;
    public DialogueController() {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(0), new RelativeSizeConstraint(0));
        this.pieces = null;
        this.position = 0;
    }
    public void set(DialoguePiece[] pieces, GameContext gameContext) {
        this.pieces = pieces;
        for (var piece : pieces) {
            piece.bindContext(gameContext);
        }
        if (pieces.length > 0)
            pieces[0].start();
        else advance();
    }

    public void advance() {
        position++;
        if (position >= pieces.length) {
            GUI.scheduleClear(0);
            GUI.toggle(62);
            return;
        }
        pieces[position].start();
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
        if (pieces.length > 0)
            pieces[position].manage(draw, pressed);
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
