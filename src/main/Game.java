package main;

import engine.core.DrawManager;
import engine.core.Screen;
import engine.jobs.JobSystem;
import engine.saving.objects.SaveFile;
import entities.Entity;
import entities.Player;
import org.joml.Vector2f;
import quests.QuestTracker;
import saving.PlayerSaver;
import saving.SavedGame;
import saving.SavedHome;
import tiles.Tile;

import java.util.List;

public class Game implements GameContext {
    public static final int TICKS_PER_SECOND = 40;
    static int ticks = 0;
    public static int getTickCount() {
        return ticks;
    }
    World world;
    final Player player;
    public final QuestTracker quests;
    private final Camera camera;
    public Player getPlayer() {
        return player;
    }
    public QuestTracker getQuests() {
        return quests;
    }
    public Game(DrawManager draw, float tsize, SaveFile file) {
        SavedGame save = new SavedGame(file, this);
        player = new Player(save.player());
        quests = new QuestTracker(save.quests());
        AutoWorldRegisterer.registerAll(this);
        world = WorldRegistry.get("HOME_WORLD");
        camera = new Camera(player, tsize, draw);
        if (save.home().home() != null)
            WorldRegistry.put("HOUSE", save.home().home());
        camera.addLight(player);
        this.attributes = new AttributeData(save.attributes());
    }
    public Game(DrawManager draw, float tsize, SavedGame save) {
        player = new Player(save.player());
        quests = new QuestTracker(save.quests());
        AutoWorldRegisterer.registerAll(this);
        world = WorldRegistry.get("HOME_WORLD");
        camera = new Camera(player, tsize, draw);
        if (save.home().home() != null)
            WorldRegistry.put("HOUSE", save.home().home());
        camera.addLight(player);
        this.attributes = new AttributeData(save.attributes());
    }
    public void tick(boolean pressed, Screen screen, float tsize, boolean canClick) {
        ++ticks;
        if (player.shouldDie(this) && !player.isDead()) {
            player.die(this);
        }
        world.tick(this, pressed, screen, tsize, canClick);
        player.tick(this);
        player.move(world.getHitbox());
        quests.update(this);

        JobSystem.tick(1f/Game.TICKS_PER_SECOND);
    }
    public void render() {
        camera.defaultBrightness = world.getBrightness();
        world.render(this, camera, camera.getTileSize());
        player.render(camera);
        camera.renderAllJobs();
    }

    @Override
    public Vector2f getPlayerRespawnPosition() {
        return new Vector2f(0, 0);
    }
    public List<Entity> getEntities() {
        return world.entities;
    }
    public void spawn(Entity entity) {
        world.spawn(entity);
    }
    public Tile getTile(int x, int y) {
        return world.map.get(x, y).orElse(null);
    }
    public SavedGame save() {
        return new SavedGame(
                PlayerSaver.savePlayer(player),
                new SavedHome(WorldRegistry.get("HOUSE")),
                quests.save(),
                attributes.save()
        );
    }
    public void movePlayer(World world, Vector2f position) {
        if (this.world != world) this.world.killOnLoadEnemies();
        this.world = world;
        world.load(camera);
        player.getPosition().set(position);
    }
    public final AttributeData attributes;

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public AttributeData attributes() {
        return attributes;
    }
}
