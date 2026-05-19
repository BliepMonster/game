package engine.jobs;

import engine.saving.ObjectSaveable;

public abstract class Job implements ObjectSaveable, Message<JobActor> {
    protected JobActor actor;
    protected boolean active = true;
    protected float time;

    public Job(JobActor actor, float time) {
        this.actor = actor;
        this.time = time;
    }

    public void tick(float delta) {
        time -= delta;
        if (isFinished())
            send(actor);
    }
    public void cancel() {
        active = false;
    }
    public boolean isFinished() {
        return time <= 0;
    }
    public boolean isInactive() {
        return isFinished() || !active;
    }
    public void send(JobActor receiver) {
        receiver.notify(this);
    }
    public float getTime() {
        return time;
    }
}
