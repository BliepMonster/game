package entities;

import main.GameContext;
import tiles.spawner.EnemySpawner;

public abstract class RespawnableEnemy implements Entity {
    private final EnemySpawner spawner;
    public RespawnableEnemy(EnemySpawner spawner) {
        this.spawner = spawner;
    }

    @Override
    public void die(GameContext context) {
        spawner.onMobDeath();
    }
}
