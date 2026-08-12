package farming;

import crafting.FullItemStorage;
import engine.core.Texture;
import engine.gui.GUI;
import engine.json.JsonNumber;
import engine.json.JsonObject;
import engine.json.JsonString;
import engine.json.JsonValue;
import farming.ui.FarmingPotComponent;
import main.Camera;
import main.GameContext;
import main.TextureIdMap;
import main.YieldUtils;
import org.joml.Vector2f;
import stats.StatHolder;
import stats.inventory.items.Item;
import stats.skills.SkillSet;
import tiles.TilePart;
import tool.editor.TilePartLoader;

import java.util.HashMap;

public class GardenPotTilePart extends TilePart {
    public PlantedCrop crop;
    private CompostType compostNextHarvest;
    public GardenPotTilePart(Vector2f position, PlantedCrop crop, CompostType compostNextHarvest) {
        super(position);
        this.crop = crop;
        this.compostNextHarvest = compostNextHarvest;
    }
    public GardenPotTilePart(Vector2f vec) {
        this(vec, null, null);
    }
    public boolean canHarvest(GameContext context) {
        return crop != null && System.currentTimeMillis() > crop.endTime();
    }
    public void harvest(GameContext context) {
        if (!canHarvest(context)) return;
        var player = context.getPlayer();
        var inv = new FullItemStorage(player.items, player.inventory, player.currency);
        crop.type().bonus().apply(inv);
        var gdrops = crop.type().guaranteed();
        for (var item : gdrops.items) {
            player.items.add(new Item(item));
        }
        var map = (HashMap<String, Integer>) gdrops.loot.clone();
        map.replaceAll((k, v) -> (int) (YieldUtils.getYieldEffect(v, player.stats.get(StatHolder.Stat.FARMING_YIELD)) * crop.compost().yieldMultiplier()));
        player.inventory.apply(map);
        player.currency.get(gdrops.coins);
        player.skills.get(SkillSet.SkillType.FARMING).getXP(crop.type().exp());
        crop = crop.type().getsDestroyed() ? null : crop;
    }
    public void plant(CropType type) {
        this.crop = new PlantedCrop(type, (long) (System.currentTimeMillis()+type.milliDuration()/(compostNextHarvest == null ? 1 : compostNextHarvest.growthBoost())), compostNextHarvest);
        compostNextHarvest = null;
    }
    public static final Texture BASE = TextureIdMap.get("base:tiles:garden_pot");

    public void render(int x, int y, Camera camera) {
        camera.addRenderJob(() -> camera.image(BASE, x, y, 1, 2.5f), -y);
        if (crop != null) {
            if (crop.compost() != null)
                camera.addRenderJob(() -> camera.image(crop.compost().overlay(), x, y, 1, 2.5f), -y);
            camera.addRenderJob(() -> camera.image(crop.endTime() > System.currentTimeMillis() ? crop.type().unready() : crop.type().ready(), x, y, 1, 2.5f), -y);
        } else if (compostNextHarvest != null) {
            camera.addRenderJob(() -> camera.image(compostNextHarvest.overlay(), x, y, 1, 2.5f), -y);
        }
    }
    public boolean isStandable() {
        return false;
    }

    @Override
    public void onStand(GameContext context) {}

    @Override
    public void onClick(GameContext context) {
        var player = context.getPlayer();
        var inv = new FullItemStorage(player.items, player.inventory, player.currency);
        if (GUI.getAll(0).isEmpty()) {
            GUI.add(new FarmingPotComponent(this, context), 0);
            GUI.toggle(62);
        }
    }

    @Override
    public JsonValue save() {
        JsonObject object = new JsonObject();
        object.values.put("type", new JsonString(TilePartLoader.ID_GARDEN_POT));
        if (crop != null) {
            object.values.put("crop", new JsonString(this.crop.type().id()));
            if (crop.compost() != null) {
                object.values.put("compost", new JsonString(this.crop.compost().id()));
            }
            object.values.put("duration", new JsonNumber(this.crop.endTime()));
        }
        if (compostNextHarvest != null) {
            object.values.put("nextCompost", new JsonString(compostNextHarvest.id()));
        }
        return object;
    }
}
