package engine.notifications;

import engine.core.text.Font;
import engine.gui.GUI;
import engine.gui.constraints.position.RelativeYConstraint;

import java.awt.Color;

public final class NotificationSystem {
    private NotificationSystem() {}
    private static final int GUI_INDEX = 63;
    private static int count = 0;
    public static Font font;
    public static void add(String text, Color color) {
        var c = count++;
        var y = c*.1f+(c == 0 ? 0 : (c-1)*.05f);
        GUI.add(new NotificationComponent(font, text, color, System.currentTimeMillis()+1000, new RelativeYConstraint(.85f-y)), GUI_INDEX);
    }
    public static void tick() {
        GUI.getAll(GUI_INDEX).removeIf(component -> ((NotificationComponent)component).deleteTime < System.currentTimeMillis());
        count = GUI.getAll(GUI_INDEX).size();
    }
}
