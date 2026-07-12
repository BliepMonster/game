package tiles.spawner;

import engine.jobs.Job;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.*;
import entities.dungeons.GemstoneGolemEnemy;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class GemstoneGolemSpawner extends EnemySpawner {
    public static final int TICKS = Game.TICKS_PER_SECOND*40;

    public GemstoneGolemSpawner(Vector2f position, GameContext context) {
        super(position, context, TICKS, "GOLD_CAVE_WORLD");
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_SPAWNER_GEMGOLEM);
    }
    public void notify(Job job) {
        this.getWorld().spawn(new GemstoneGolemEnemy(new Vector2f(this.position), this));
    }
}
