package tiles.npc;

import crafting.ItemRequirement;

public record ShopEntry(ItemRequirement[] requirements, MoneyRequirement price, ItemGiver item) {}
