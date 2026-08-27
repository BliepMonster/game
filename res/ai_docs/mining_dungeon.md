# Sapphire Mine Dungeon Design Document

## Overview

A 6-floor mines-themed dungeon accessed from the Sapphire Cave (`SAPPHIRE_CAVE`).
Each floor is its own world file of type `"dungeon"` with `DungeonExitTilePart` gates
between floors. Death in any floor sends the player back to `HOME_WORLD`.

**Entry point**: New entrance object in `sapphire_cave.json` leading to Floor 1 (`SAPPHIRE_DUNGEON_1`).
**Exit from Floor 6**: `DungeonExitTilePart` back to `SAPPHIRE_CAVE`.

---

## Dungeon Structure

```
SAPPHIRE_CAVE
  └─ entrance → SAPPHIRE_DUNGEON_1 (Floor 1)
                   └─ dungeon_exit → SAPPHIRE_MINE_F2 (Floor 2)
                                       └─ dungeon_exit → SAPPHIRE_MINE_F3 (Floor 3)
                                                            └─ dungeon_exit → SAPPHIRE_MINE_F4 (Floor 4)
                                                                                 └─ dungeon_exit → SAPPHIRE_MINE_F5 (Floor 5)
                                                                                                      └─ dungeon_exit → SAPPHIRE_MINE_F6 (Floor 6)
                                                                                                                           └─ dungeon_exit → SAPPHIRE_CAVE
```

Each floor is a separate JSON map file and world. Every floor uses `"type": "dungeon"`.

---

## Global Settings

| Property | Value |
|----------|-------|
| Floor 1 brightness | 0.3 (already set in `sapphire_dung1.json`) |
| Floors 2-6 brightness | 0.12-0.20 (per-floor, listed below) |
| World IDs | Floor 1: `SAPPHIRE_DUNGEON_1`; Floors 2-6: `SAPPHIRE_MINE_F2`-`SAPPHIRE_MINE_F6` |
| Map files | Floor 1: `res/maps/sapphire_dung1.json` (existing, scene-based); Floor 2: `res/maps/sapphire_dung2.json` (scene-based); Floors 3-6: `res/maps/sapphire_mine_f3.json`-`sapphire_mine_f6.json` |
| Recommended tile size per floor | ~600-900 tiles |

> Floor 1 already exists as `sapphire_dung1.json` and uses the **scene** tile system
> (a 12x15 grid slicing `res/textures/scenes/sapphire_dungeon_001.png`; tile keys are
> `base:scenes:sapphire_f1:x:y` instead of `WALL`/`ROAD`). Floor 2 follows the same
> **scene** system slicing `res/textures/scenes/sapphire_dungeon_002.png`
> (`base:scenes:sapphire_dung2`, already registered in `res/texids/tiles/scenes.json`).
> Floors 3-6 are built with the standard `WALL`/`ROAD` tile layout.

---

## New Enemies

### 1. Miner Zombie

| Property | Value |
|----------|-------|
| Class | `MinerZombieEnemy` extends `MeleeDungeonEnemy` |
| HP | 220 |
| Damage | 28 |
| Defense | 25 |
| Speed | 2.5 |
| Aggro / Deaggro | 8 / 14 |
| Hit Interval | 12 ticks |
| XP | 35 |
| Knockback Resistance | `max(log(25)/100, 1) = 1` |
| Render | Dark grey-brown 1x1 rect, red when hurt |
| Loot | COMMON: 5 coins + 1 "Mine Fragment"; RARE: 1x "Mine Fragment" (bonus); LEGENDARY: 1x `base:armor:miner_boots` |

> Stat block is slightly tougher than the first draft (HP 200→220, DMG 25→28,
> DEF 20→25, XP 30→35) so Floor 1's mobs hit a bit harder.

### 2. Cave Bat

| Property | Value |
|----------|-------|
| Class | `CaveBatEnemy` extends `DungeonEnemy` |
| HP | 120 |
| Damage | 15 (projectile) |
| Defense | 0 |
| Speed | N/A (stationary shooter) |
| Aggro / Deaggro | N/A (always shoots when player is in range) |
| Shoot Interval | 50 ticks |
| Projectile | `BasicArrowProjectile` at speed 0.35, 15 damage, NORMAL type |
| XP | 20 |
| Knockback Resistance | 1 |
| Render | Dark purple 1x1 rect, smaller hitbox (0.6x0.6) |
| Loot | COMMON: 3 coins; LEGENDARY: 1x `base:armor:miner_leggings` |

### 3. Crystal Golem

| Property | Value |
|----------|-------|
| Class | `CrystalGolemEnemy` extends `MeleeDungeonEnemy` |
| HP | 400 |
| Damage | 40 |
| Defense | 200 |
| Speed | 1.5 |
| Aggro / Deaggro | 6 / 10 |
| Hit Interval | 35 ticks |
| XP | 80 |
| Knockback Resistance | `max(log(200)/100, 1) = 1` |
| Damage Reduction | NORMAL: `damage * (1 - 200/300) = 33%` of original |
| Render | Bright blue-cyan 1x1 rect, red when hurt |
| Loot | COMMON: 1x "Sapphire Shard" |

### 4. Dynamite Goblin

| Property | Value |
|----------|-------|
| Class | `DynamiteGoblinEnemy` extends `DungeonEnemy` |
| HP | 180 |
| Damage | 50 (explosion on impact) |
| Defense | 10 |
| Speed | 3 |
| Aggro / Deaggro | 10 / 16 |
| Shoot Interval | 60 ticks |
| Projectile | `DynamiteProjectile` at speed 0.35 |
| Explosion | Radius 2.5, knockback 3, DamageType.EXPLOSION |
| XP | 45 |
| Knockback Resistance | `max(log(10)/100, 1) = 1` |
| Render | Orange 1x1 rect, red when hurt |
| Behavior | Stationary shooter like CaveBat, throws dynamite at player |
| Loot | COMMON: 5 coins; UNCOMMON: 1x "Mine Fragment" |

### 5. Mine Overseer (Floor 6 boss)

| Property | Value |
|----------|-------|
| Class | `MineOverseerEnemy` extends `MeleeDungeonEnemy` |
| HP | 800 |
| Damage | 55 |
| Defense | 150 |
| Speed | 2 |
| Aggro / Deaggro | 12 / 20 |
| Hit Interval | 25 ticks |
| XP | 200 |
| Knockback Resistance | `max(log(150)/100, 1) = 1` |
| Damage Reduction | NORMAL: `damage * (1 - 150/250) = 40%` of original |
| Render | Dark red-brown 1.2x1.2 rect, red when hurt |
| Loot | LEGENDARY: 1x `base:weapons:pickaxe_blade`; EPIC: 1x `base:armor:miner_vest`; COMMON: 20 coins + 3x "Mine Fragment" |

### 6. Tunnel Worm (new entry for Floor 4+)

| Property | Value |
|----------|-------|
| Class | `TunnelWormEnemy` extends `MeleeDungeonEnemy` |
| HP | 300 |
| Damage | 30 |
| Defense | 80 |
| Speed | 2 |
| Aggro / Deaggro | 7 / 12 |
| Hit Interval | 15 ticks |
| XP | 50 |
| Knockback Resistance | `max(log(80)/100, 1) = 1` |
| Render | Dark green 1x1 rect, red when hurt |
| Loot | COMMON: 1x "Mine Fragment"; UNCOMMON: 2x "Mine Fragment" |

---

## New Hazards

### Exploding Minecart (Floor 1 hazard)

| Property | Value |
|----------|-------|
| Class | `ExplodingMinecartEntity` implements `Entity` (in `src/entities`, alongside `ExplosionEntity`) |
| Type | Hazard, NOT an `Enemy` |
| isAttackable | `false` — cannot be damaged, and does NOT block the `DungeonExit` kill-gate |
| Hitbox | 0.8x0.8 |
| Speed | 0.5 tiles/tick along the track (straight line) |
| Detonation | `ExplosionEntity(damage=45, kb=4, radius=2.5)` — `EXPLOSION` type, bypasses defense |
| XP / Loot | None |
| Render | Small brown 1x1 cart; flashes orange-red during the final ticks before detonating |

Behavior:
1. A `MinecartSpawner` adds one to the world on an interval.
2. It rolls in a straight line along the track in the direction it was spawned with.
3. It detonates when it touches the player, reaches the end of its `distance`, or its
   lifetime (fuse) expires. Detonation spawns an `ExplosionEntity`.
4. `damage()` and `knock()` are no-ops — the cart is dodgeable, not fightable.

### MinecartSpawner (`src/tiles/spawner/MinecartSpawner.java`)

A `TilePart implements JobActor` placed at the start of a track segment through the
map's `objects` array. It uses the same `ResourceJob` pattern as `EnemySpawner` to
re-add a cart every `interval` ticks.

```json
{ "x": <track_start_x>, "y": <track_start_y>, "object": {
    "type": "minecart_spawner",
    "direction": "right",
    "interval": 300,
    "distance": 10
} }
```

- `direction`: `up` / `down` / `left` / `right` (the direction the track runs).
- `interval`: ticks between carts (300 = 5 seconds).
- `distance`: tiles the cart travels before auto-detonating (set to the track length).

Requires a new `TilePartLoader` entry: `ID_MINECART_SPAWNER = "minecart_spawner"`.

---

## New Projectile

### DynamiteProjectile

| Property | Value |
|----------|-------|
| Class | `DynamiteProjectile` extends `SimpleProjectile` |
| Speed | 0.35 |
| Hitbox | 0.3x0.3 |
| Render | Small orange rect |
| On impact | Spawns `ExplosionEntity(damage=50, kb=3, radius=2.5)` |
| On die | Calls `super.die()` (no-op) |
| Sender | Stored as `Entity sender` — explosion damages EVERYTHING in radius (including other enemies and player) |

---

## New Items

### Weapons

#### Pickaxe Blade

| Property | Value |
|----------|-------|
| ID | `base:weapons:pickaxe_blade` |
| Name | Pickaxe Blade |
| Type | `melee` |
| Melee Type | `cone` |
| Damage | 55 |
| Knockback | 1.8 |
| Cooldown | 28 ticks |
| Range | 4.2 |
| Angle | 24 degrees |
| Damage Type | NORMAL |
| Stats | `[]` (none) |
| Texture | `base:item:pickaxe_blade` |

### Equipment

#### Miner's Vest

| Property | Value |
|----------|-------|
| ID | `base:armor:miner_vest` |
| Name | Miner's Vest |
| Type | `equipment` |
| Slot | `CHESTPLATE` |
| Stats | DEFENSE: 40, MINING_YIELD: 5, STRENGTH: 8 |
| Texture | `base:item:miner_vest` |

#### Miner's Leggings

| Property | Value |
|----------|-------|
| ID | `base:armor:miner_leggings` |
| Name | Miner's Leggings |
| Type | `equipment` |
| Slot | `LEGGINGS` |
| Stats | DEFENSE: 30, MINING_YIELD: 5, SPEED: 1 |
| Texture | `base:item:miner_leggings` |
| Source | Drop-only (Cave Bat, LEGENDARY 2%) — no crafting recipe |

#### Miner's Boots

| Property | Value |
|----------|-------|
| ID | `base:armor:miner_boots` |
| Name | Miner's Boots |
| Type | `equipment` |
| Slot | `BOOTS` |
| Stats | DEFENSE: 20, MINING_YIELD: 5, KNOCKBACK: 3 |
| Texture | `base:item:miner_boots` |
| Source | Drop-only (Miner Zombie, LEGENDARY 2%) — no crafting recipe |

#### Crystal Pendant

| Property | Value |
|----------|-------|
| ID | `base:rings:crystal_pendant` |
| Name | Crystal Pendant |
| Type | `equipment` |
| Slot | `RING` |
| Stats | MAX_HEALTH: 15, CRIT_CHANCE: 5 |
| Texture | `base:item:crystal_pendant` |

### Consumables

#### Mining Draught

| Property | Value |
|----------|-------|
| ID | `base:potions:mine_potion` |
| Name | Mining Draught |
| Type | `consumable` |
| Effect Stats | MINING_POWER: 30, MINING_YIELD: 10 |
| Duration | 3600 ticks (60 seconds) |
| Effect UID | `mining_draught` |
| Texture | `base:item:mine_potion` |

---

## New Resources

| Resource Name | Dropped By | Usage |
|---------------|-----------|-------|
| Mine Fragment | Miner Zombie (COMMON), Dynamite Goblin (UNCOMMON), Tunnel Worm (COMMON) | Pickaxe Blade recipe, Miner's Vest recipe |
| Sapphire Shard | Crystal Golem (COMMON) | Crystal Pendant recipe, Mining Draught recipe |

---

## Floor-by-Floor Breakdown

### Floor 1: Mine Entrance

**World ID**: `SAPPHIRE_DUNGEON_1`
**Map file**: `res/maps/sapphire_dung1.json` (scene-based, already present)
**Brightness**: 0.3 (already set in the map file)
**Theme**: Wooden support beams, open entrance cavern with a **minecart track** running through it. Exploding carts launch from spawners at regular intervals.
**Tile layout**: 12x15 scene grid. The rail track is drawn into the scene texture; wall tiles carry an `unstandable` part.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie (tougher stat block) | 3 | Scattered in main room |

**Hazards (objects):**

| Object | Count | Detail |
|--------|-------|--------|
| MinecartSpawner | 2 | One at each end of the track, facing inward; ~300 tick interval, distance = track length |

**DungeonExit**: Leads to `SAPPHIRE_MINE_F2` at spawn (0,0).

**Design note**: The mobs use the slightly-tougher Miner Zombie stat block, and the
exploding carts force the player to keep moving — standing on the track is punished.

**Lore feeling**: "The old sapphire mine stretches deep below. Something stirred the workers down here..."

---

### Floor 2: The Swarm Room

**World ID**: `SAPPHIRE_MINE_F2`
**Map file**: `res/maps/sapphire_dung2.json` (scene-based — slices `sapphire_dungeon_002.png` via `base:scenes:sapphire_dung2`)
**Brightness**: 0.12
**Theme**: One large rectangular room (~20x30) swarming with Miner Zombies and Cave Bats. A small wooden platform sits in the room as decor — the intended future home for loot barrels (not yet implemented).
**Tile layout**: Large open rectangular room, low obstacle density, a small wooden platform near one wall.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie | 6-8 (large swarm) | Spread across the room |
| Cave Bat | 5-6 | Perched around the room |

**DungeonExit**: Leads to `SAPPHIRE_MINE_F3` at spawn (0,0).

**Design note**: The room is **less crowded than Floor 1** — fewer enemies per tile and
plenty of open floor to maneuver. This intentionally buffs an evasive / magic playstyle:
players dodge bat shots and kite the zombie swarm through open space instead of
face-tanking. The legs/boots drops (Cave Bat / Miner Zombie, LEGENDARY 2%) fit the swarm
fantasy here — high kill counts make the chase items farmable.

---

### Floor 3: Crystal Cavern

**World ID**: `SAPPHIRE_MINE_F3`
**Brightness**: 0.18 (slightly brighter from crystal glow)
**Theme**: Open cavern with crystal formations (sapphire ore nodes as decoration).
**Tile layout**: Large open room (~20x15) with pillar obstacles and a side corridor.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie | 2 | Main room |
| Cave Bat | 1 | Elevated alcove |
| Crystal Golem | 1 | Center of main room |

**DungeonExit**: Leads to `SAPPHIRE_MINE_F4` at spawn (0,0).

**Design note**: First appearance of Crystal Golem. The open room lets the player maneuver around its slow speed, but its high defense demands sustained damage.

---

### Floor 4: The Blast Zone

**World ID**: `SAPPHIRE_MINE_F4`
**Brightness**: 0.10
**Theme**: Crumbling tunnels with evidence of explosions. Debris and broken tracks.
**Tile layout**: Broken corridors with gaps, multiple branching paths, two small rooms connected by a narrow passage.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie | 2 | Corridors |
| Dynamite Goblin | 2 | Each in a separate room |
| Tunnel Worm | 1 | Corridor between rooms |

**DungeonExit**: Leads to `SAPPHIRE_MINE_F5` at spawn (0,0).

**Design note**: First appearance of Dynamite Goblin and Tunnel Worm. The goblins' explosions can hit the Tunnel Worm and Miner Zombies, creating chaotic fights. The player must position carefully to avoid being caught between explosions.

---

### Floor 5: The Deep Shaft

**World ID**: `SAPPHIRE_MINE_F5`
**Brightness**: 0.08 (very dark)
**Theme**: Vertical shaft-like layout, spiraling downward. Dense enemy encounters.
**Tile layout**: Spiral corridor pattern around a central pit (unstandable), tight spaces.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie | 4 | Spiral corridors |
| Cave Bat | 2 | Opposite sides of spiral |
| Tunnel Worm | 1 | Near the exit |
| Crystal Golem | 1 | Guarding the exit gate |

**DungeonExit**: Leads to `SAPPHIRE_MINE_F6` at spawn (0,0).

**Design note**: The darkest floor. Multiple enemy types at once. The Crystal Golem guarding the exit forces the player to defeat it before descending to the final floor.

---

### Floor 6: The Overseer's Chamber

**World ID**: `SAPPHIRE_MINE_F6`
**Brightness**: 0.20 (lit by lava/orange glow)
**Theme**: The deepest chamber. Industrial mining equipment, the boss arena.
**Tile layout**: Large rectangular arena (~25x20) with two side alcoves for adds.

**Enemies (onLoad):**

| Enemy | Count | Position (approx) |
|-------|-------|--------------------|
| Miner Zombie | 2 | Side alcoves (1 each) |
| Mine Overseer | 1 | Center of arena |

**DungeonExit**: Leads back to `SAPPHIRE_CAVE` at spawn (0,-1).

**Boss behavior**: Mine Overseer is a slow, devastating melee enemy. At 50% HP, the two Miner Zombies spawn in the alcoves as reinforcements (placed via onLoad, alive from the start but far enough away that they only aggro if the player moves near them or the boss knockback pushes the player toward them).

**Loot on kill**:

| Rarity | Chance | Reward |
|--------|--------|--------|
| COMMON | 50% | 20 coins + 3x Mine Fragment |
| LEGENDARY | 2% | 1x Pickaxe Blade |
| EPIC | 6% | 1x Miner's Vest |

---

## Crafting Recipes

### Anvil Recipes

#### Pickaxe Blade

```json
{
  "name": "Pickaxe Blade",
  "inputs": [
    { "type": "resource", "id": "Mine Fragment", "amount": 3 },
    { "type": "upgrade", "id": "base:tools:sapphire_pickaxe", "amount": 1 }
  ],
  "outputs": [
    { "type": "item", "id": "base:weapons:pickaxe_blade", "amount": 1 }
  ],
  "requirements": [
    { "type": "skill", "skill": "MINING", "level": 14 },
    { "type": "skill", "skill": "COMBAT", "level": 10 }
  ]
}
```

### Crafting Table Recipes

#### Miner's Vest

```json
{
  "name": "Miner's Vest",
  "inputs": [
    { "type": "resource", "id": "Mine Fragment", "amount": 6 },
    { "type": "upgrade", "id": "base:armor:steel_chestplate", "amount": 1 }
  ],
  "outputs": [
    { "type": "item", "id": "base:armor:miner_vest", "amount": 1 }
  ],
  "requirements": [
    { "type": "skill", "skill": "COMBAT", "level": 10 }
  ]
}
```

#### Crystal Pendant

```json
{
  "name": "Crystal Pendant",
  "inputs": [
    { "type": "resource", "id": "Sapphire Shard", "amount": 2 },
    { "type": "resource", "id": "Refined Sapphire", "amount": 1 }
  ],
  "outputs": [
    { "type": "item", "id": "base:rings:crystal_pendant", "amount": 1 }
  ],
  "requirements": [
    { "type": "skill", "skill": "MINING", "level": 14 }
  ]
}
```

### Alchemy Station Recipes

#### Mining Draught

```json
{
  "recipe": "res/recipes/alchemy_back/mine_potion.json",
  "xp": 100,
  "time": 8000,
  "tex": "base:item:mine_potion"
}
```

Alchemy back recipe:
```json
{
  "inputs": [
    { "type": "resource", "id": "Glowing Mushroom", "amount": 5 },
    { "type": "resource", "id": "Sapphire", "amount": 2 }
  ],
  "outputs": [
    { "type": "item", "id": "base:potions:mine_potion", "amount": 1 }
  ],
  "requirements": [
    { "type": "skill", "skill": "ALCHEMY", "level": 8 }
  ]
}
```

---

## Complete Enemy Summary Table

| Enemy | HP | DMG | Defense | Speed | Hit Interval | XP | Floors |
|-------|-----|-----|---------|-------|-------------|----|--------|
| Miner Zombie | 220 | 28 | 25 | 2.5 | 12 | 35 | 1,2,3,4,5,6 |
| Cave Bat | 120 | 15 | 0 | - | 50 (ranged) | 20 | 2,3,5 |
| Crystal Golem | 400 | 40 | 200 | 1.5 | 35 | 80 | 3,5 |
| Dynamite Goblin | 180 | 50 | 10 | 3 | 60 (ranged) | 45 | 4 |
| Tunnel Worm | 300 | 30 | 80 | 2 | 15 | 50 | 4,5 |
| Mine Overseer | 800 | 55 | 150 | 2 | 25 | 200 | 6 (boss) |
| Exploding Minecart (hazard) | N/A | 45 | N/A | 0.5 | detonates | N/A | 1 |

---

## Complete Loot Summary Table

| Enemy | COMMON (50%) | UNCOMMON (30%) | RARE (12%) | EPIC (6%) | LEGENDARY (2%) |
|-------|-------------|----------------|------------|-----------|----------------|
| Miner Zombie | 5 coins + 1 Mine Fragment | — | 1 Mine Fragment (bonus) | — | 1 Miner's Boots |
| Cave Bat | 3 coins | — | — | — | 1 Miner's Leggings |
| Crystal Golem | 1 Sapphire Shard | — | — | — | — |
| Dynamite Goblin | 5 coins | 1 Mine Fragment | — | — | — |
| Tunnel Worm | 1 Mine Fragment | 2 Mine Fragment | — | — | — |
| Mine Overseer | 20 coins + 3 Mine Fragment | — | — | 1 Miner's Vest | 1 Pickaxe Blade |

---

## Required New Files

### Java Source Files

| # | File Path | Description |
|---|-----------|-------------|
| 1 | `src/entities/dungeons/MinerZombieEnemy.java` | Melee enemy, extends MeleeDungeonEnemy |
| 2 | `src/entities/dungeons/CaveBatEnemy.java` | Ranged enemy, extends DungeonEnemy |
| 3 | `src/entities/dungeons/CrystalGolemEnemy.java` | Heavy melee, extends MeleeDungeonEnemy |
| 4 | `src/entities/dungeons/DynamiteGoblinEnemy.java` | Ranged AoE, extends DungeonEnemy |
| 5 | `src/entities/dungeons/TunnelWormEnemy.java` | Melee, extends MeleeDungeonEnemy |
| 6 | `src/entities/dungeons/MineOverseerEnemy.java` | Boss melee, extends MeleeDungeonEnemy |
| 7 | `src/entities/projectiles/DynamiteProjectile.java` | Explosive projectile, extends SimpleProjectile |
| 8 | `src/entities/ExplodingMinecartEntity.java` | Floor 1 rolling hazard, implements Entity |
| 9 | `src/tiles/spawner/MinecartSpawner.java` | Periodic minecart spawner (TilePart + JobActor) |

### Map Files

| # | File Path | Description |
|---|-----------|-------------|
| 10 | `res/maps/sapphire_dung1.json` | Floor 1: Mine Entrance (already exists) |
| 11 | `res/maps/sapphire_dung2.json` | Floor 2: Swarm Room (scene-based, uses `sapphire_dungeon_002.png`) |
| 12 | `res/maps/sapphire_mine_f3.json` | Floor 3: Crystal Cavern |
| 13 | `res/maps/sapphire_mine_f4.json` | Floor 4: Blast Zone |
| 14 | `res/maps/sapphire_mine_f5.json` | Floor 5: Deep Shaft |
| 15 | `res/maps/sapphire_mine_f6.json` | Floor 6: Overseer's Chamber |

### Item JSON Files

| # | File Path | Description |
|---|-----------|-------------|
| 16 | `res/items/weapons/pickaxe_blade.json` | Melee weapon |
| 17 | `res/items/armor/miner_vest.json` | Chestplate equipment |
| 18 | `res/items/armor/miner_leggings.json` | Leggings equipment (Cave Bat drop) |
| 19 | `res/items/armor/miner_boots.json` | Boots equipment (Miner Zombie drop) |
| 20 | `res/items/rings/crystal_pendant.json` | Ring equipment |
| 21 | `res/items/potions/mine_potion.json` | Consumable potion |

### Recipe JSON Files

| # | File Path | Description |
|---|-----------|-------------|
| 22 | `res/recipes/anvil/pickaxe_blade.json` | Weapon recipe |
| 23 | `res/recipes/crafting_table/miner_vest.json` | Armor recipe |
| 24 | `res/recipes/crafting_table/crystal_pendant.json` | Ring recipe |
| 25 | `res/recipes/alchemy/mine_potion.json` | Alchemy recipe |
| 26 | `res/recipes/alchemy_back/mine_potion.json` | Alchemy back-end recipe |

### Modified Files

| # | File Path | Change |
|---|-----------|--------|
| 27 | `res/maps/sapphire_cave.json` | Add entrance object: `{ "x": <x>, "y": <y>, "object": { "type": "entrance", "world_id": "SAPPHIRE_DUNGEON_1", "x": 0, "y": 0, "texture_id": "res/bred.png" } }` |
| 28 | `res/crafting/anvil.json` | Add `"res/recipes/anvil/pickaxe_blade.json"` to recipe list |
| 29 | `res/crafting/crafting_table.json` | Add `"res/recipes/crafting_table/miner_vest.json"` and `"res/recipes/crafting_table/crystal_pendant.json"` to recipe list |
| 30 | `src/tool/editor/TilePartLoader.java` | Add `ID_MINECART_SPAWNER = "minecart_spawner"` + load case: `new MinecartSpawner(pos, context, direction, interval, distance)` |

**Item and enemy JSON are auto-discovered** (`StaticItemLoader` finds items; map
`onLoad` entries construct enemies by class name) — no code work for those. The ONLY
`TilePartLoader` change is registering the `minecart_spawner` object type.

---

## Player Progression Path

```
Player reaches Sapphire Cave (needs MINING 14, Sapphire Pickaxe)
  → Enters Floor 1 (difficulty baseline)
    → Floors 2-5 (escalating challenge)
      → Floor 6: Defeat Mine Overseer
        → Returns to Sapphire Cave with loot
          → Crafts Pickaxe Blade (55 dmg weapon, upgrade from Sapphire Pickaxe)
          → Crafts Miner's Vest (40 def + utility stats)
          → Crafts Crystal Pendant (HP + crit ring)
          → Crafts Mining Draught (temporary mining buff)
```

---

## Difficulty Curve

```
Floor 1: █████░░░░░░░░░░░░░░░  Easy (3 tougher melee + exploding minecart hazards)
Floor 2: ██████░░░░░░░░░░░░░░  Medium (large open swarm room — evasive/magic favored)
Floor 3: ██████████░░░░░░░░░░  Medium-Hard (Crystal Golem + ranged)
Floor 4: █████████████░░░░░░░  Hard (AoE explosions + new enemy types)
Floor 5: ████████████████░░░░  Very Hard (dense enemies, dark, all types)
Floor 6: ████████████████████  Boss (Mine Overseer + adds, best loot)
```

---

## Balance Notes

- **Mine Fragment** is the core dungeon currency. A player who clears all 6 floors should earn roughly 12-18 fragments (enough for 2-3 of the new items).
- **Sapphire Shard** is rarer (only from Crystal Golem, floors 3 and 5 = 2 possible). A full clear yields ~2 shards. This gates the Crystal Pendant behind multiple runs.
- **Pickaxe Blade** is the trophy drop (LEGENDARY 2% from boss only). Multiple runs may be needed.
- **Miner's Vest** is the EPIC drop (6% from boss). Should drop roughly once every 16-17 boss kills.
- The dungeon is designed for players with Steel/Sapphire tier gear and COMBAT level ~10-14.
- **Self-damage from Dynamite Goblin**: The explosion damages the player too, so positioning matters. The goblin's explosions will also damage other enemies, which creates emergent gameplay (let goblins thin the herd, but risk getting caught in the blast).
- **Minecarts are hazards, not enemies**: they are non-attackable, so they don't block
  the `DungeonExit` kill-gate and grant no XP/loot. Their explosions deal `EXPLOSION`
  damage (bypasses defense), so standing on the track is punished even with high armor.
- **Floor 1 difficulty**: with brightness 0.3, the slightly-tougher Miner Zombies, and
  periodic exploding carts, Floor 1 is a gentle-but-lived-in gate to the mine.
