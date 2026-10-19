package tiles.npc;

import crafting.ItemRequirement;
import crafting.PreCraftRequirement;
import engine.core.textures.Texture;

public record ShopEntry(PreCraftRequirement[] preCraft, ItemRequirement[] requirements, MoneyRequirement price, ItemGiver item, Texture texture) {}
