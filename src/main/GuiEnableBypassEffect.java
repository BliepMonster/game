package main;

import engine.core.DrawManager;
import engine.gui.GUI;

import java.util.Optional;

public class GuiEnableBypassEffect implements CameraEffect {
    private final long millis, start;

    public GuiEnableBypassEffect(long millis) {
        this.start = System.currentTimeMillis();
        this.millis = millis;
    }

    @Override
    public Optional<CameraEffect> getSuccessor(GameContext context) {
        GUI.enableAll();
        return Optional.empty();
    }

    @Override
    public float getTimeLeft() {
        return (start+millis-System.currentTimeMillis())/1000f;
    }

    @Override
    public void draw(DrawManager draw) {}
}
