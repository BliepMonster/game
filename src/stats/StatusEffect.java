package stats;

import main.Game;
import saving.SavedStatusEffect;

public class StatusEffect {
    public final StatHolder stats;
    public final int endTick;
    public final String id;
    public StatusEffect(String uid, StatHolder stats, int duration) {
        this.stats = stats;
        this.id = uid;
        this.endTick = Game.getTickCount()+duration;
    }
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StatusEffect effect)) return false;
        return id.equals(effect.id);
    }

    public boolean isDone() {
        return Game.getTickCount() > endTick;
    }
    public SavedStatusEffect save() {
        return new SavedStatusEffect(endTick-Game.getTickCount(), id, stats);
    }
    public StatusEffect(SavedStatusEffect effect) {
        this(effect.uid(), effect.stats(), effect.remaining());
    }
}
