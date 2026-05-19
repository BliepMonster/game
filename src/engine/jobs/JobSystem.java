package engine.jobs;

import java.util.ArrayList;

public final class JobSystem {
    private static final ArrayList<Job> jobs = new ArrayList<>();
    private JobSystem() {}
    public static void tick(float delta) {
        for (var job : jobs) {
            job.tick(delta);
        }
        jobs.removeIf(Job::isInactive);
    }
    public static void addJob(Job job) {
        jobs.add(job);
    }
    public static void clear() {
        jobs.clear();
    }
}
