package tiles.npc.dialogue;

import engine.gui.components.Component;
import engine.gui.constraints.position.PositionConstraint;
import engine.gui.constraints.size.SizeConstraint;

public abstract class DialoguePiece extends Component {
    private DialogueController controller;

    public DialoguePiece(PositionConstraint x, PositionConstraint y, SizeConstraint width, SizeConstraint height, DialogueController controller) {
        super(x, y, width, height);
        this.controller = controller;
    }

    public void advance() {
        controller.advance();
    }
}
