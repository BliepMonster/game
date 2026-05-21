package tiles.resources;

import engine.jobs.Job;
import engine.saving.objects.SavedObject;

public class ResourceJob extends Job {
    public ResourceJob(ResourceCreator callback, float time) {
        super(callback, time);
    }
    public SavedObject save() {
        throw new UnsupportedOperationException();
    }
}
