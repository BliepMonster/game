package engine.jobs;

public interface Message<T extends Receiver<?>> {
    void send(T receiver);
}
