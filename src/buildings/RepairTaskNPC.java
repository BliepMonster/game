package buildings;

import crafting.FullItemStorage;
import engine.gui.GUI;
import main.Camera;
import main.GameContext;
import org.joml.Vector2f;
import tiles.TilePart;
import tiles.npc.NpcDefinition;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import tiles.npc.dialogue.DialoguePiece;
import tiles.npc.dialogue.DialogueRepairController;
import tool.editor.TilePartLoader;

import static main.Main.font;

public class RepairTaskNPC extends TilePart {
    public final NpcDefinition definition;
    public final RepairTask task;
    private Camera camera;

    public RepairTaskNPC(Vector2f position, NpcDefinition definition, RepairTask task) {
        super(position);
        this.definition = definition;
        this.task = task;
    }

    @Override
    public void render(int x, int y, Camera camera) {
        this.camera = camera;
        camera.addRenderJob(
                () -> camera.image(definition.texture(), x, y, 1, definition.height()),
                -y
        );
    }

    @Override
    public boolean isStandable() {
        return true;
    }

    @Override
    public void onStand(GameContext context) {}

    @Override
    public void onClick(GameContext context) {
        if (GUI.getAll(0).isEmpty()) {
            var player = context.getPlayer();
            var storage = new FullItemStorage(player.items, player.inventory, player.currency);
            var controller = new DialogueRepairController(task, font, camera, storage);
            DialoguePiece[] pieces = new DialoguePiece[definition.pieces().length];
            for (int i = 0; i < pieces.length; i++) {
                pieces[i] = definition.pieces()[i].build(font, controller, context, storage);
            }
            controller.set(pieces);
            GUI.add(controller, 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString(TilePartLoader.ID_REPAIRER));
        object.values.put("id", new JsonString(definition.id()));
        object.values.put("task", new JsonString(task.id()));
        return object;
    }
}
