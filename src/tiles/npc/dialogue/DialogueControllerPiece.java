package tiles.npc.dialogue;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.gui.GUI;
import engine.gui.constraints.position.CenterConstraint;
import engine.gui.constraints.size.RelativeSizeConstraint;
import main.GameContext;

public class DialogueControllerPiece extends DialoguePiece {
    protected int position;
    protected DialoguePiece[] pieces;
    public DialogueControllerPiece(DialoguePiece[] pieces, DialogueController controller) {
        super(CenterConstraint.CENTER, CenterConstraint.CENTER, new RelativeSizeConstraint(0), new RelativeSizeConstraint(0), controller);
        this.pieces = pieces;
        this.position = 0;
    }
    public void bindContext(GameContext gameContext) {
        set(pieces, gameContext);
    }
    public void set(DialoguePiece[] pieces, GameContext gameContext) {
        this.pieces = pieces;
        for (var piece : pieces) {
            piece.setController(new DialogueController() {
                                    @Override
                                    public void advance() {
                                        DialogueControllerPiece.this.advance();
                                    }
                                    @Override
                                    public void set(DialoguePiece[] pieces, GameContext gameContext) {}
            });
            piece.bindContext(gameContext);
        }
        if (pieces.length > 0)
            pieces[0].start();
        else advance();
    }

    public void advance() {
        position++;
        if (position >= pieces.length) {
            super.advance();
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
