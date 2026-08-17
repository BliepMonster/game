package main;

import engine.core.DrawManager;
import engine.gui.GUI;

import java.awt.*;
import java.util.Optional;

public class FadeAttributeEffect implements CameraEffect {
    static class FadeOut implements CameraEffect {
        private final long millis, start;

        public FadeOut(long millis) {
            this.start = System.currentTimeMillis();
            this.millis = millis;
        }

        @Override
        public float getTimeLeft() {
            return (start+millis-System.currentTimeMillis())/1000f;
        }
        public void draw(DrawManager draw) {
            draw.rect(new Color(0, 0, 0, (start+millis-System.currentTimeMillis())/(float)millis), -8, -8, 16, 16); // technically -aspect by -2, not 8 but whatever
        }

        @Override
        public Optional<CameraEffect> getSuccessor(GameContext context) {
            return Optional.of(new GuiEnableBypassEffect(1000));
        }
    }
    private final long millis, start;
    private final String attrib;

    public FadeAttributeEffect(String attrib, long millis) {
        this.attrib = attrib;
        GUI.disableAll();
        this.start = System.currentTimeMillis();
        this.millis = millis;
    }

    @Override
    public float getTimeLeft() {
        return (start+millis-System.currentTimeMillis())/1000f;
    }
    public void draw(DrawManager draw) {
        draw.rect(new Color(0, 0, 0, 1-(start+millis-System.currentTimeMillis())/(float)millis), -8, -8, 16, 16); // technically -aspect by -2, not 8 but whatever
    }

    @Override
    public Optional<CameraEffect> getSuccessor(GameContext context) {
        context.attributes().add(attrib);
        return Optional.of(new FadeOut(millis));
    }
}
