package engine.jobs;

public interface Sender<M extends Message<?>, R extends Receiver<?>> {
    void send(M message, R receiver);
}
