package engine.saving;

import java.util.Base64;
import java.nio.ByteBuffer;

public class HexConverter {

    public static String encodeFloat(float value) {
        return Float.toString(value);
    }

    public static float decodeFloat(String str) {
        return Float.parseFloat(str);
    }
}
