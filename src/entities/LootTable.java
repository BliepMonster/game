package entities;

import crafting.FullItemStorage;

import java.util.EnumMap;
import java.util.Random;

public class LootTable {
    private final Random random = new Random();
    private final EnumMap<LootType, LootReward[]> map = new EnumMap<>(LootType.class);
    public LootTable(LootPiece... pieces) {
        for (LootPiece piece : pieces) {
            var reward = map.get(piece.type());
            if (reward == null)
                map.put(piece.type(), new LootReward[]{piece.reward()});
            else {
                var arr = new LootReward[reward.length+1];
                System.arraycopy(reward, 0, arr, 0, reward.length);
                arr[reward.length] = piece.reward();
                map.put(piece.type(), arr);
            }
        }
    }
    public void apply(FullItemStorage storage) {
        double value = Math.random();
        switch (value) {
            case double v1 when v1 < 0.5 -> {
                var vv = map.get(LootType.COMMON);
                if (vv == null) return;
                int index = random.nextInt(0, vv.length);
                var v = vv[index];
                if (v != null) {
                    v.apply(storage);
                }
            }
            case double v1 when v1 < 0.8 -> {
                var vv = map.get(LootType.UNCOMMON);
                if (vv == null) return;
                int index = random.nextInt(0, vv.length);
                var v = vv[index];
                if (v != null) {
                    v.apply(storage);
                }
            }
            case double v1 when v1 < 0.92 -> {
                var vv = map.get(LootType.RARE);
                if (vv == null) return;
                int index = random.nextInt(0, vv.length);
                var v = vv[index];
                if (v != null) {
                    v.apply(storage);
                }
            }
            case double v1 when v1 < 0.98 -> {
                var vv = map.get(LootType.EPIC);
                if (vv == null) return;
                int index = random.nextInt(0, vv.length);
                var v = vv[index];
                if (v != null) {
                    v.apply(storage);
                }
            }
            default -> {
                var vv = map.get(LootType.LEGENDARY);
                if (vv == null) return;
                int index = random.nextInt(0, vv.length);
                var v = vv[index];
                if (v != null) {
                    v.apply(storage);
                }
            }
        }
    }
}
