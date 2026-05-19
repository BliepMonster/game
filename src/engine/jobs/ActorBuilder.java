package engine.jobs;

import engine.saving.objects.SavedArray;

public interface ActorBuilder {
    JobActor build(SavedArray arr);
}
