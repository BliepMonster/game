package engine.jobs;

import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedFloat;
import engine.saving.objects.SavedObject;

import java.util.ArrayList;

public class SimpleJob extends Job {
    public SimpleJob(JobActor actor, float time) {
        super(actor, time);
    }
    public SimpleJob(SavedArray save) {
        super(JobActor.fromSave((SavedArray) save.get(0)), (float) save.get(1).get());
    }
    public SavedObject save() {
        ArrayList<SavedObject> list = new ArrayList<>();
        list.add(new SavedFloat(time));
        return new SavedArray(list);
    }
}
