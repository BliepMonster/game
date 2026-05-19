package engine.core.input;

public class Console {
    public final Input input = new Input();
    public void press(int key) {
        input.events.add(new Event(EventManager.KEY_DOWN, key));
    }
}
