package tiles.spawner;

import engine.jobs.Job;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.dungeons.GemstoneGolemEnemy;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class GemstoneGolemSpawner extends EnemySpawner {
    public static final int TICKS = Game.TICKS_PER_SECOND*180;

    public GemstoneGolemSpawner(Vector2f position, GameContext context) {
        super(position, context, TICKS, "GEMSTONE_CAVE");
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_SPAWNER_GEMGOLEM);
    }
    public void notify(Job job) {
        this.getWorld().spawn(new GemstoneGolemEnemy(new Vector2f(this.position), this));
    }
}
