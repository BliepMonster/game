package engine.core.input;

import java.util.ArrayList;

public class EventManager {
    public static final int KEY_DOWN = 0;
    public static final int KEY_UP = 1;
    public static final int MOUSE_DOWN = 2;
    public static final int MOUSE_UP = 3;
    private final ArrayList<Event> eventList = new ArrayList<>();
    public void add(Event e) {
        eventList.add(e);
    } public ArrayList<Event> get() {
        return eventList;
    } public void clear() {
        eventList.clear();
    }
}
