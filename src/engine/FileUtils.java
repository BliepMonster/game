package engine;

import java.io.FileReader;
import java.io.IOException;

public final class FileUtils {
    private FileUtils() {}
    public static String readAllOrEmpty(String path) {
        try (FileReader reader = new FileReader(path)) {
            return reader.readAllAsString();
        } catch (IOException e) {
            return "";
        }
    }
}
