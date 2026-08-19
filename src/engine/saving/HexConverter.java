package engine.saving;

import java.util.Base64;
import java.nio.ByteBuffer;

public class HexConverter {

    public static String encodeDouble(double value) {
        return Double.toString(value);
    }

    public static double decodeDouble(String str) {
        return Double.parseDouble(str);
    }
}
