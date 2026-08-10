package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.GUI;
import engine.gui.components.Component;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;

public class DialogueController extends Component {
    private int position;
    private DialoguePiece[] pieces;
    public DialogueController() {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(0), new RelativeSizeConstraint(0));
        this.pieces = null;
        this.position = 0;
    }
    public void set(DialoguePiece[] pieces) {
        this.pieces = pieces;
    }

    public void advance() {
        position++;
        if (position >= pieces.length) {
            GUI.scheduleClear(0);
            GUI.toggle(62);
        }
    }

    @Override
    public void manage(DrawManager draw, boolean pressed) {
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
