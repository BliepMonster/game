package tiles.strawberries;

import engine.jobs.Job;
import engine.saving.objects.SavedObject;

public class StrawberryCreationJob extends Job {
    public static final float TIME_STRAWBERRY = 5f;
    public SavedObject save() {
        throw new UnsupportedOperationException();
    }
    public StrawberryCreationJob(StrawberryTilePart callback) {
        super(callback, TIME_STRAWBERRY);
    }
}
