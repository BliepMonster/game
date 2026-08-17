package tiles.npc.dialogue;

import buildings.RepairTask;
import buildings.ui.RepairTaskDisplayPanel;
import crafting.FullItemStorage;
import engine.core.text.Font;
import engine.gui.GUI;
import main.Camera;

public class DialogueRepairController extends DialogueController {
    private final RepairTask task;
    private final Font font;
    private final Camera camera;
    private final FullItemStorage storage;

    public DialogueRepairController(RepairTask task, Font font, Camera camera, FullItemStorage storage) {
        super();
        this.task = task;
        this.font = font;
        this.camera = camera;
        this.storage = storage;
    }

    @Override
    public void advance() {
        position++;
        if (position >= pieces.length) {
            GUI.clear(0);
            GUI.add(new RepairTaskDisplayPanel(task, font, camera, storage), 0);
            return;
        }
        pieces[position].start();
    }
}
