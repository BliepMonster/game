package quests;

import main.GameContext;
import saving.SavedQuests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class QuestTracker {
    private String selectedId;
    private final HashSet<String> completed = new HashSet<>();
    private final HashSet<String> claimed = new HashSet<>();
    private final HashMap<String, Integer> kills = new HashMap<>();
    private final HashMap<String, Integer> buys = new HashMap<>();
    private final HashMap<String, Integer> gathers = new HashMap<>();
    private final HashMap<String, Integer> talks = new HashMap<>();
    private final HashSet<String> itemCrafts = new HashSet<>(), resourceCrafts = new HashSet<>();
    private int sells;

    public QuestTracker(SavedQuests save) {
        this.selectedId = save.selectedId();
        if (save.completedIds() != null) completed.addAll(Arrays.asList(save.completedIds()));
        if (save.claimedIds() != null) claimed.addAll(Arrays.asList(save.claimedIds()));
        if (save.kills() != null) kills.putAll(save.kills());
        if (save.buys() != null) buys.putAll(save.buys());
        if (save.gathers() != null) gathers.putAll(save.gathers());
        if (save.talks() != null) talks.putAll(save.talks());
        this.sells = save.sells();
    }

    public void select(Quest quest) {
        selectedId = quest.getId();
    }

    public Quest getSelected() {
        return selectedId == null ? null : QuestRegistry.get(selectedId);
    }

    public boolean isSelected(Quest quest) {
        return quest.getId().equals(selectedId);
    }

    public boolean isCompleted(String id) {
        return completed.contains(id);
    }

    public boolean isClaimed(String id) {
        return claimed.contains(id);
    }

    public void claim(String id, GameContext context) {
        if (!completed.contains(id)) throw new RuntimeException("Quest not completed: " + id);
        if (claimed.contains(id)) throw new RuntimeException("Quest already claimed: " + id);
        Quest quest = QuestRegistry.get(id);
        ObjectiveReward[] rewards = quest.getRewards();
        if (rewards != null) {
            for (ObjectiveReward reward : rewards) {
                if (reward != null) reward.grant(context);
            }
        }
        claimed.add(id);
    }

    private static boolean hasNoRewards(Quest quest) {
        ObjectiveReward[] rewards = quest.getRewards();
        return rewards == null || rewards.length == 0;
    }

    public void onKill(String enemy) {
        kills.put(enemy, kills.getOrDefault(enemy, 0) + 1);
    }

    public int getKills(String enemy) {
        return kills.getOrDefault(enemy, 0);
    }

    public void onBuy(String itemId) {
        buys.put(itemId, buys.getOrDefault(itemId, 0) + 1);
    }

    public int getBuys(String itemId) {
        return buys.getOrDefault(itemId, 0);
    }
    public void onItemCraft(String item) {
        itemCrafts.add(item);
    }
    public void onResourceCraft(String resource) {
        resourceCrafts.add(resource);
    }

    public void onSell() {
        sells++;
    }

    public int getSells() {
        return sells;
    }

    public void onGather(String item) {
        gathers.put(item, gathers.getOrDefault(item, 0) + 1);
    }

    public int getGathers(String item) {
        return gathers.getOrDefault(item, 0);
    }
    public boolean hasItemBeenCrafted(String item) {
        return itemCrafts.contains(item);
    }
    public boolean hasResourceBeenCrafted(String item) {
        return resourceCrafts.contains(item);
    }

    public void onTalk(String npcId) {
        talks.put(npcId, talks.getOrDefault(npcId, 0) + 1);
    }

    public int getTalks(String npcId) {
        return talks.getOrDefault(npcId, 0);
    }

    public void update(GameContext context) {
        for (Quest quest : QuestRegistry.all()) {
            if (!completed.contains(quest.getId()) && quest.isUnlocked(this) && quest.isDone(context)) {
                completed.add(quest.getId());
            }
            if (completed.contains(quest.getId()) && !claimed.contains(quest.getId()) && hasNoRewards(quest)) {
                claim(quest.getId(), context);
            }
        }
        if (selectedId != null && completed.contains(selectedId)) {
            selectedId = null;
        }
    }

    public SavedQuests save() {
        return new SavedQuests(
                selectedId,
                completed.toArray(new String[0]),
                claimed.toArray(new String[0]),
                new HashMap<>(kills),
                new HashMap<>(buys),
                new HashMap<>(gathers),
                new HashMap<>(talks),
                sells,
                itemCrafts.toArray(new String[0]),
                resourceCrafts.toArray(new String[0])
        );
    }
}
