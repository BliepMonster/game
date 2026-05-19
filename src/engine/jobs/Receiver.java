package engine.jobs;

public interface Receiver<T extends Message<?>> {
    void notify(T message);
}
