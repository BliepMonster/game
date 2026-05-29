package entities;

import main.GameContext;
import org.joml.Vector2f;
import tiles.spawner.EnemySpawner;

public abstract class RespawnableEnemy extends Enemy {
    private final EnemySpawner spawner;
    public RespawnableEnemy(Vector2f position, EnemySpawner spawner) {
        super(new Vector2f(position));
        this.spawner = spawner;
    }

    @Override
    public void die(GameContext context) {
        spawner.onMobDeath();
    }
}
