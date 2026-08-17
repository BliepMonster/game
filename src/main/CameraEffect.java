package main;

import engine.core.Drawable;

import java.util.Optional;

public interface CameraEffect extends Drawable {
    float getTimeLeft();
    Optional<CameraEffect> getSuccessor(GameContext context);
}
