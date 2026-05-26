package tiles;

import engine.jobs.Job;
import engine.json.JsonString;
import engine.json.JsonValue;
import entities.ZombieEnemy;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import tool.editor.TilePartLoader;

public class ZombieSpawner extends EnemySpawner {
    private final Vector2f position;
    public static final int TICKS = Game.TICKS_PER_SECOND*20;

    public ZombieSpawner(Vector2f position, GameContext context) {
        super(context, TICKS);
        this.position = position;
    }

    @Override
    public JsonValue save() {
        return new JsonString(TilePartLoader.ID_ZOMBIE_SPAWNER);
    }
    public void notify(Job job) {
        this.context.spawn(new ZombieEnemy(this.position, this));
    }
}
