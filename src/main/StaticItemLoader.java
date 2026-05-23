package main;

import combat.melee.ConeWeaponDefinition;
import combat.melee.MeleeWeaponItemDefinition;
import combat.melee.SpecificStabWeaponDefinition;
import engine.core.Texture;
import entities.DamageType;
import stats.inventory.items.BasicFoodItemDefinition;
import stats.inventory.items.ItemDefinition;

public class StaticItemLoader {
    public static final Texture BREAD_TEXTURE = new Texture("res/bred.png");
    public static final String BREAD_NAME = "Bread";
    public static final String BREAD_DESCRIPTION = "A piece of bread";
    public static final String BREAD_ITEM_ID = "base:bread";
    public static final String DAGGER_ITEM_ID = "base:dagger";
    public static final String SWORD_ITEM_ID = "base:sword";
    public static final String CLUB_ITEM_ID = "base:club";
    public static final MeleeWeaponItemDefinition SWORD_ITEM_DEF = new ConeWeaponDefinition(1.5f, DamageType.NORMAL, 30, 5, (float) Math.toRadians(20), 3*Game.TICKS_PER_SECOND/5, "Sword", "A sword", SWORD_ITEM_ID, new Texture("res/bred.png"));
    public static final MeleeWeaponItemDefinition DAGGER_ITEM_DEF = new SpecificStabWeaponDefinition(10, entities.DamageType.NORMAL, Game.TICKS_PER_SECOND/5, "Dagger", "A stab", DAGGER_ITEM_ID, new Texture("res/bred.png"));
    public static final MeleeWeaponItemDefinition CLUB_ITEM_DEF = new ConeWeaponDefinition(8, DamageType.NORMAL, 15, 4, (float) Math.toRadians(35), 3*Game.TICKS_PER_SECOND, "Club", "A club", CLUB_ITEM_ID, new Texture("res/bred.png"));
    public static void initAll() {
        ItemDefinition.register(new BasicFoodItemDefinition(BREAD_ITEM_ID, BREAD_NAME, BREAD_DESCRIPTION, BREAD_TEXTURE, 75));
    }
}
