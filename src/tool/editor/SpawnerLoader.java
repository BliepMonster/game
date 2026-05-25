package tool.editor;

import entities.ZombieEnemy;
import main.Game;
import main.GameContext;
import org.joml.Vector2f;
import org.joml.Vector2i;
import tiles.EnemySpawner;

public final class SpawnerLoader {
    public static EnemySpawner loadSpawner(Vector2i position, GameContext context) {
        return new EnemySpawner(
                (spawner) -> new ZombieEnemy(new Vector2f(position.x, position.y), spawner),
                context,
                Game.TICKS_PER_SECOND*15
        );
    }
}
