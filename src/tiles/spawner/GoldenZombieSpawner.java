package tiles.spawner;

import engine.jobs.Job;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.*;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class GoldenZombieSpawner extends EnemySpawner {
    public static final int TICKS = Game.TICKS_PER_SECOND*180;

    public GoldenZombieSpawner(Vector2f position, GameContext context) {
        super(position, context, TICKS, "GOLD_CAVE_WORLD");
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_GOLDEN_ZOMBIE_SPAWNER);
    }
    public void notify(Job job) {
        this.getWorld().spawn(new GoldenZombieEnemy(new Vector2f(this.position), this));
    }
}
