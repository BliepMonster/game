package farming;

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
import stats.inventory.items.ItemDefinition;
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

    @Override
    public boolean canBreak() {
        return crop == null;
    }

    public void harvest(GameContext context) {
        if (!canHarvest(context)) return;
        var player = context.getPlayer();
        crop.type().bonus().apply(context);
        var gdrops = crop.type().guaranteed();
        for (var item : gdrops.items) {
            player.items.add(new Item(item));
        }
        var map = (HashMap<String, Integer>) gdrops.loot.clone();
        map.replaceAll((_, v) -> YieldUtils.getYieldEffect(YieldUtils.getYieldEffect(v, player.stats.get(StatHolder.Stat.FARMING_YIELD)), (crop.compost() == null ? 1 : crop.compost().yieldMultiplier()*100-100)));
        player.inventory.apply(map);
        player.currency.get(gdrops.coins);
        player.skills.get(SkillSet.SkillType.FARMING).getXP(crop.type().exp());
        crop = crop.type().getsDestroyed() ? null : new PlantedCrop(crop.type(), System.currentTimeMillis()+crop.type().milliDuration(), crop.compost());
    }
    public void plant(CropType type) {
        this.crop = new PlantedCrop(type, (long) (System.currentTimeMillis()+type.milliDuration()/(compostNextHarvest == null ? 1 : (double) compostNextHarvest.growthBoost())), compostNextHarvest);
        compostNextHarvest = null;
    }
    public void destroyCrop() {
        this.crop = null;
    }
    public void setCompostNextHarvest(CompostType compost) {
        this.compostNextHarvest = compost;
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
    @Override
    public ItemDefinition getItemDefinition() {
        return ItemDefinition.get("base:placers:garden_pot");
    }
}
