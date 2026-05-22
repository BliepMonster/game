package stats.inventory.items;

import java.util.Optional;

public record ItemUseResult(Optional<Item> leftover, boolean handled) {}
