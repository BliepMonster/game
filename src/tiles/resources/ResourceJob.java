package tiles.resources;

import engine.jobs.Job;
import engine.jobs.JobActor;
import engine.saving.objects.SavedObject;

public class ResourceJob extends Job {
    public ResourceJob(JobActor callback, float time) {
        super(callback, time);
    }
    public SavedObject save() {
        throw new UnsupportedOperationException();
    }
}
