package engine.jobs;

import engine.saving.ObjectSaveable;
import engine.saving.objects.SavedArray;

public interface JobActor extends Receiver<Job> {
    static JobActor fromSave(SavedArray arr) {
        String id = (String) arr.get(0).get();
        var b = ActorRegistry.get(id);
        if (b == null)
            throw new RuntimeException("No actor builder found for \"" + id + "\"");
        return b.build(arr);
    }
}
