package main;

import combat.melee.ConeWeaponDefinition;
import combat.melee.SpecificStabWeaponDefinition;
import engine.core.Texture;
import entities.DamageType;
import stats.inventory.items.BasicFoodItemDefinition;
import stats.inventory.items.ToolItemDefinition;

import static stats.inventory.items.ItemDefinition.register;

public class StaticItemLoader {
    public static final Texture BREAD_TEXTURE = new Texture("res/bred.png");
    public static final String BREAD_NAME = "Bread";
    public static final String BREAD_DESCRIPTION = "A piece of bread";
    public static final String BREAD_ITEM_ID = "base:food:bread";
    public static final String DAGGER_ITEM_ID = "base:weapons:dagger";
    public static final String SWORD_ITEM_ID = "base:weapons:sword";
    public static final String CLUB_ITEM_ID = "base:weapons:club";
    public static final String RUSTY_AXE_ITEM_ID = "base:tools:rusty_axe";
    public static final String STEEL_PICKAXE_ITEM_ID = "base:tools:steel_pickaxe";
    public static final String RUSTY_PICKAXE_ITEM_ID = "base:tools:rusty_pickaxe";
    public static void initAll() {
        register(new BasicFoodItemDefinition(BREAD_ITEM_ID, BREAD_NAME, BREAD_DESCRIPTION, BREAD_TEXTURE, 75));
        register(new ConeWeaponDefinition(1.5f, DamageType.NORMAL, 30, 5, (float) Math.toRadians(20), 3*Game.TICKS_PER_SECOND/5, "Sword", "A sword", SWORD_ITEM_ID, new Texture("res/bred.png")));
        register(new SpecificStabWeaponDefinition(10, entities.DamageType.NORMAL, Game.TICKS_PER_SECOND/5, "Dagger", "A stab", DAGGER_ITEM_ID, new Texture("res/bred.png")));
        register(new ConeWeaponDefinition(8, DamageType.NORMAL, 15, 4, (float) Math.toRadians(35), 3*Game.TICKS_PER_SECOND, "Club", "A club", CLUB_ITEM_ID, new Texture("res/bred.png")));
        register(new ToolItemDefinition(Game.TICKS_PER_SECOND/5, RUSTY_AXE_ITEM_ID, "Rusty Axe", "", BREAD_TEXTURE, new ToolItemDefinition.PowerPair(ToolItemDefinition.PowerType.WOODCUTTING, 10)));
        register(new ToolItemDefinition(Game.TICKS_PER_SECOND, STEEL_PICKAXE_ITEM_ID, "Steel Pickaxe", "", new Texture("res/steel_pickaxe.png"), new ToolItemDefinition.PowerPair(ToolItemDefinition.PowerType.MINING, 100)));
        register(new ToolItemDefinition(Game.TICKS_PER_SECOND*2/3, RUSTY_PICKAXE_ITEM_ID, "Rusty Pickaxe", "", new Texture("res/rusty_pickaxe.png"), new ToolItemDefinition.PowerPair(ToolItemDefinition.PowerType.MINING, 40)));
    }
}
