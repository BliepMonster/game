package engine;

import engine.core.Screen;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {
    public static final float PIXEL_SIZE = .1f/16f;
    public static Matrix4f loadTransform(Vector3f position, Vector3f scale) {
        return new Matrix4f().identity()
                .translate(position)
                .scale(scale);
    }
    public static Matrix4f loadTransform(Matrix4f dest, Vector3f position, Vector3f scale) {
        return dest.identity()
                .translate(position)
                .scale(scale);
    }
    public static int nearestPowerOfTwo(int value) {
        return (int) Math.pow(2, Math.round(Math.log(value) / Math.log(2)));
    }
    public static String countdownString(long millisRemaining) {
        long totalSeconds = Math.max(0, (millisRemaining+999)/1000);
        if (totalSeconds >= 3600) return String.format("%d:%02d:%02d", totalSeconds/3600, totalSeconds%3600/60, totalSeconds%60);
        return String.format("%d:%02d", totalSeconds/60, totalSeconds%60);
    }
    public static int toScreenPixels(Screen screen, float GLWidth) {
        return (int) (GLWidth*screen.getHeight()/2);
    }
    public static float toGLCoordinates(Screen screen, int pixels) {
        return (pixels/(float)screen.getHeight())*2;
    }
    public static float nearestPowerOf2FromGLCoordinates(float GLCoordinates, Screen screen) {
        return toGLCoordinates(screen, nearestPowerOfTwo(toScreenPixels(screen, GLCoordinates)));
    }
}
